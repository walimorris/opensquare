package com.morris.opensquare.services.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import com.morris.opensquare.models.OpensquareMultipartFile;
import com.morris.opensquare.services.FileService;
import com.morris.opensquare.services.ImageTrackingService;
import com.morris.opensquare.services.loggers.LoggerService;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImageTrackingServiceImpl implements ImageTrackingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTrackingServiceImpl.class);

    private final FileService fileService;
    private final LoggerService loggerService;
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


    @Autowired
    public ImageTrackingServiceImpl(FileService fileService, LoggerService loggerService,
                                    ApplicationPropertiesConfiguration applicationPropertiesConfiguration) {
        this.fileService = fileService;
        this.loggerService = loggerService;
        this.applicationPropertiesConfiguration = applicationPropertiesConfiguration;
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
            }
        }
        if (exifImageMetaDataMap.containsKey(LATITUDE) && exifImageMetaDataMap.containsKey(LONGITUDE)) {
            GeocodingResult result = locateFromLatLong(exifImageMetaDataMap);

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

        } catch (InterruptedException | ApiException e) {
            throw new RuntimeException(e);
        }
        return result[0];
    }

    private File getImageFile(String path) {
        return fileService.getFile(path);
    }

    private void printTagValue(JpegImageMetadata jpegImageMetadata, TagInfo tagInfo) throws ImageReadException {
        TiffField field = jpegImageMetadata.findEXIFValueWithExactMatch(tagInfo);

        try {
            LOGGER.info("{}: {}", tagInfo.name, field.getValue());
        } catch (ImageReadException e) {
            loggerService.saveLog(
                    e.getClass().getName(),
                    e.getMessage(),
                    Optional.of(LOGGER)
            );
        }
    }
}
