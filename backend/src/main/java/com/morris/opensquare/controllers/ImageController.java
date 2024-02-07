package com.morris.opensquare.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    @GetMapping("/*")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request) throws IOException {
        String requestUri = request.getRequestURI();
        LOGGER.info("Streaming image from path: {}", requestUri);
        ClassPathResource imageFile = new ClassPathResource(requestUri);
        byte[] bytes = StreamUtils.copyToByteArray(imageFile.getInputStream());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }
}
