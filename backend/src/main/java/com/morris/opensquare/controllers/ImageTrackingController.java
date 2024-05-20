package com.morris.opensquare.controllers;

import com.morris.opensquare.models.trackers.VisionPulse;
import com.morris.opensquare.services.ImageTrackingService;
import com.morris.opensquare.services.impl.OpenAiServiceImpl;
import org.apache.commons.imaging.ImageReadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/opensquare/api/imageTracking")
public class ImageTrackingController {
    private final ImageTrackingService imageTrackingService;
    private final OpenAiServiceImpl openAiService;

    @Autowired
    public ImageTrackingController(ImageTrackingService imageTrackingService, OpenAiServiceImpl openAiService) {
        this.imageTrackingService = imageTrackingService;
        this.openAiService = openAiService;
    }

    @PostMapping("/scan")
    public ResponseEntity<Map<String, Object>> scanImage(@RequestParam MultipartFile file) throws IOException, ImageReadException {
        Map<String, Object> result = imageTrackingService.getExifImageMetaData(file);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    // TODO: possible uploads should be urls, base64, using byte[] streams from multiple-part uploads (see above)
    // Depending on the image type: options should be given. Possible meta scrape or user input for extra details
    // to append to the metadata field. These can help the model give more informed vision details.
    @GetMapping("/vision_pulse")
    public ResponseEntity<String> visionPulse(@RequestParam String url, @RequestParam String q) {
        VisionPulse visionPulse = VisionPulse.builder()
                .imageUrl(url)
                .text(q)
                .metaData(null)
                .build();

        String response = openAiService.processVisionPulse(visionPulse);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
