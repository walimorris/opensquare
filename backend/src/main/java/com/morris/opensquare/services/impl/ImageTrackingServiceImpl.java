package com.morris.opensquare.services.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import com.morris.opensquare.models.documents.OpensquareMultipartFile;
import com.morris.opensquare.models.exceptions.ImageTrackingServiceRunTimeException;
import com.morris.opensquare.services.ImageTrackingService;
import com.morris.opensquare.services.loggers.LoggerService;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

// TODO: Create functionality to allow persistence of updated image files. Example: an analyst
// TODO: needs to track an image and only acquires small pieces of data at a time (a team has
// TODO: found the GPS coordinates that weren't originally there) they can now add that metadata
// TODO: to a file, persist that file and continue building pieces until they have enough to
// TODO: give actionable insight. Appended data should follow an image. This data can come from
// TODO: various *credible sources, but the intent is that it is built and persisted until it
// TODO: (the image) can provide credible/actionable insight.

//TODO: Feature: explore option to prompt AI about landmarks in close vicinity based on a image
@Service
public class ImageTrackingServiceImpl implements ImageTrackingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTrackingServiceImpl.class);
    private final ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    private static final String IMAGE_JPEG = "image/jpeg";
    private static final String IMAGE_JPG = "image/jpg";
    private static final String NAME = "name";
    private static final String CONTENT_TYPE = "contentType";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String FORMATTED_ADDRESS = "formattedAddress";
    private static final String PLACE_ID = "placeId";
    private static final String COMPOUND_CODE = "compoundCode";
    private static final String GLOBAL_CODE = "globalCode";
    private final LoggerService loggerService;


    @Autowired
    public ImageTrackingServiceImpl(ApplicationPropertiesConfiguration applicationPropertiesConfiguration,
                                    LoggerService loggerService) {
        this.applicationPropertiesConfiguration = applicationPropertiesConfiguration;
        this.loggerService = loggerService;
    }

    @Override
    public Map<String, Object> getExifImageMetaData(MultipartFile multipartFile) throws IOException, ImageReadException {
        OpensquareMultipartFile imageMultiPartFile = new OpensquareMultipartFile(multipartFile);
        LOGGER.info("image content type={}", imageMultiPartFile.getContentType());
        Map<String, Object> exifImageMetaDataMap = new HashMap<>();
        if (Objects.equals(imageMultiPartFile.getContentType(), IMAGE_JPEG) ||
                Objects.equals(imageMultiPartFile.getContentType(), IMAGE_JPG)) {

            ImageMetadata metadata = Imaging.getMetadata(imageMultiPartFile.getBytes());
            JpegImageMetadata jpegImageMetadata = (JpegImageMetadata) metadata;

            if (jpegImageMetadata != null) {
                TiffImageMetadata exifMetadata = jpegImageMetadata.getExif();
                TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();

                // get fields
                double latitude = gpsInfo == null ? 0 : gpsInfo.getLatitudeAsDegreesNorth();
                double longitude = gpsInfo == null ? 0 : gpsInfo.getLongitudeAsDegreesEast();

                // build map structure
                exifImageMetaDataMap.put(NAME, imageMultiPartFile.getName());
                exifImageMetaDataMap.put(CONTENT_TYPE, imageMultiPartFile.getContentType());
                exifImageMetaDataMap.put(LATITUDE, latitude);
                exifImageMetaDataMap.put(LONGITUDE, longitude);

                // let's collect remaining TIFF (Tag Image File Format) containing graphics info
                appendTiffMetadata(exifImageMetaDataMap, jpegImageMetadata);
            }
        }
        return addLongLatProperties(exifImageMetaDataMap);
    }

    private Map<String, Object> addLongLatProperties(Map<String, Object> exifImageMetaDataMap) throws IOException {
        if (exifImageMetaDataMap.containsKey(LATITUDE) && exifImageMetaDataMap.containsKey(LONGITUDE)) {
            GeocodingResult result = locateFromLatLong(exifImageMetaDataMap);

            if (result != null) {
                // properties
                String formattedAddress = result.formattedAddress == null ? null : result.formattedAddress;
                String placeId = result.placeId == null ? null : result.placeId;
                String compoundCode = result.plusCode.compoundCode == null ? null : result.plusCode.compoundCode;
                String globalCode = result.plusCode.globalCode == null ? null : result.plusCode.globalCode;

                exifImageMetaDataMap.put(FORMATTED_ADDRESS, formattedAddress);
                exifImageMetaDataMap.put(PLACE_ID, placeId);
                exifImageMetaDataMap.put(COMPOUND_CODE, compoundCode);
                exifImageMetaDataMap.put(GLOBAL_CODE, globalCode);
            }
        }
        return exifImageMetaDataMap;
    }

    private GeocodingResult locateFromLatLong(Map<String, Object> metadata) throws IOException {
        String mapsApiKey = applicationPropertiesConfiguration.googleMapsApiKey();
        GeocodingResult[] result;
        try (GeoApiContext mapsContext = new GeoApiContext.Builder()
                .apiKey(mapsApiKey)
                .build()) {

            LatLng latLng = new LatLng((Double) metadata.get(LATITUDE), (Double) metadata.get(LONGITUDE));
            result = GeocodingApi.reverseGeocode(mapsContext, latLng).await();

        } catch (InterruptedException e) {
            loggerService.saveLog(
                    e.getClass().getName(),
                    "Interrupted getting Lat/Long with Google API: " + e.getMessage(),
                    Optional.ofNullable(LOGGER)
            );
            Thread.currentThread().interrupt();
            return null;
        } catch (ApiException ex) {
            throw new ImageTrackingServiceRunTimeException(ex.getMessage(), ex);
        }
        return result[0];
    }

    // TODO: Need to build a class to record all possible pieces of metadata in order to
    // TODO: build comprehensible context to feed to large language model for interpretation.
    // TODO: This class should be stored (along with the persisted image).
    private static void appendTiffMetadata(Map<String, Object> exifMetaDataMap, JpegImageMetadata jpegImageMetadata) {
        List<TagInfo> tiffTags = TiffTagConstants.ALL_TIFF_TAGS;
        for (TagInfo tag : tiffTags) {
            TiffField field = jpegImageMetadata.findEXIFValueWithExactMatch(tag);
            if (field == null) {
                System.out.println("No info for: " + tag.name);
            } else {
                exifMetaDataMap.put(tag.name, field.getValueDescription());
            }
        }
    }
}
