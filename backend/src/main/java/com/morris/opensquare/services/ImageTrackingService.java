package com.morris.opensquare.services;

import org.apache.commons.imaging.ImageReadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ImageTrackingService {

    /**
     * Get Exif, Tiff and other image metadata.
     *
     * @param file {@link MultipartFile}
     * @return {@link Map} of image metadata and their values
     *
     * @throws IOException exception reading metadata
     * @throws ImageReadException exception reading image
     */
    Map<String, Object> getExifImageMetaData(MultipartFile file) throws IOException, ImageReadException;

    /**
     * Get Base64 encoded String from {@link MultipartFile} file.
     *
     * @param file {@link MultipartFile} file
     *
     * @return {@link String} Base64 encoded String
     */
    String base64partEncodedStr(MultipartFile file);
}
