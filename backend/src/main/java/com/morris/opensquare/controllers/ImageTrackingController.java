package com.morris.opensquare.controllers;

import com.morris.opensquare.services.ImageTrackingService;
import org.apache.commons.imaging.ImageReadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/opensquare/api/imageTracking")
public class ImageTrackingController {
    private final ImageTrackingService imageTrackingService;

    @Autowired
    public ImageTrackingController(ImageTrackingService imageTrackingService) {
        this.imageTrackingService = imageTrackingService;
    }

    @PostMapping("/scan")
    public ResponseEntity<Map<String, Object>> scanImage(@RequestParam MultipartFile file) throws IOException, ImageReadException {
        Map<String, Object> result = imageTrackingService.getExifImageMetaData(file);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }
}
