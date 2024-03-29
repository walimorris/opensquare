package com.morris.opensquare.controllers;

import com.morris.opensquare.services.OwaspReferenceService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/opensquare")
public class OwaspReferenceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OwaspReferenceController.class);
    private final OwaspReferenceService owaspReferenceService;

    @Autowired
    public OwaspReferenceController(OwaspReferenceService owaspReferenceService) {
        this.owaspReferenceService = owaspReferenceService;
    }

    @CrossOrigin(origins = "https://cloud.mongodb.com")
    @GetMapping("/owasp/posts")
    public ResponseEntity<String> getLatestOwaspBlog(HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        LOGGER.info("{}", "API triggered");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(owaspReferenceService.getOwaspBlogSnippetsList());
    }
}
