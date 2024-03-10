package com.morris.opensquare.services;

import org.apache.commons.imaging.ImageReadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ImageTrackingService {

    Map<String, Object> getExifImageMetaData(MultipartFile file) throws IOException, ImageReadException;
}
