package com.morris.opensquare.controllers;

import com.morris.opensquare.models.ai.VisionPulse;
import com.morris.opensquare.services.ImageTrackingService;
import com.morris.opensquare.services.impl.OpenAiServiceImpl;
import com.morris.opensquare.utils.FileUtil;
import org.apache.commons.imaging.ImageReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/opensquare/api/imageTracking")
public class ImageTrackingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTrackingController.class);
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
    public ResponseEntity<String> visionPulse(@RequestParam MultipartFile f, @RequestParam String q) throws IOException, ImageReadException {
        Map<String, Object> metadata = imageTrackingService.getExifImageMetaData(f);
        String base64EncodedString = FileUtil.base64partEncodedStr(f);

        VisionPulse visionPulse = VisionPulse.builder()
                .imageUrl(base64EncodedString)
                .text(q)
                .metaData(metadata)
                .build();

        String response = openAiService.processVisionPulse(visionPulse);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/generate_vision_pulse")
    public ResponseEntity<URI> generateVisionPulse(@RequestParam MultipartFile f, @RequestParam String q) {
        String base64EncodedString = FileUtil.base64partEncodedStr(f);

        VisionPulse visionPulse = VisionPulse.builder()
                .imageUrl(base64EncodedString)
                .text(q)
                .build();

        URI response = openAiService.generateImageFromInputImage(visionPulse);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/generate_vision_pulse_from_prompt")
    public ResponseEntity<URI> generateVisionPulseFromPrompt(@RequestParam String q) {
        VisionPulse visionPulse = VisionPulse.builder()
                .text(q)
                .build();
        URI response = openAiService.generateImageFromPrompt(visionPulse);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
