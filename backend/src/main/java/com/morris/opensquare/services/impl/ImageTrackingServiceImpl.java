package com.morris.opensquare.services.impl;

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
    private static Logger LOGGER = LoggerFactory.getLogger(ImageTrackingServiceImpl.class);

    private final FileService fileService;
    private final LoggerService loggerService;

    @Autowired
    public ImageTrackingServiceImpl(FileService fileService, LoggerService loggerService) {
        this.fileService = fileService;
        this.loggerService = loggerService;
    }

    @Override
    public Map<String, Object> getExifImageMetaData(MultipartFile multipartFile) throws IOException, ImageReadException {
        OpensquareMultipartFile imageMultiPartFile = new OpensquareMultipartFile(multipartFile);
        LOGGER.info("image content type={}", imageMultiPartFile.getContentType());
        Map<String, Object> exifImageMetaDataMap = new HashMap<>();
        if (Objects.equals(imageMultiPartFile.getContentType(), "image/jpeg") ||
                Objects.equals(imageMultiPartFile.getContentType(), "image/jpg")) {

            ImageMetadata metadata = Imaging.getMetadata(imageMultiPartFile.getBytes());
            if (metadata instanceof JpegImageMetadata jpegImageMetadata) {
                TiffImageMetadata exifMetadata = jpegImageMetadata.getExif();
                TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();

                // get fields
                double latitude = gpsInfo == null ? 0 : gpsInfo.getLatitudeAsDegreesNorth();
                double longitude = gpsInfo == null ? 0 : gpsInfo.getLongitudeAsDegreesEast();

                // build map structure
                exifImageMetaDataMap.put("name", imageMultiPartFile.getName());
                exifImageMetaDataMap.put("contentType", imageMultiPartFile.getContentType());
                exifImageMetaDataMap.put("latitude", latitude);
                exifImageMetaDataMap.put("longitude", longitude);
            }
        }
        return exifImageMetaDataMap;
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
                    Optional.ofNullable(LOGGER)
            );
        }
    }
}
