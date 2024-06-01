package com.morris.opensquare.controllers;

import com.morris.opensquare.models.documents.Document;
import com.morris.opensquare.services.impl.DocumentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/opensquare/api/reports")
public class ReportsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsController.class);

    private final DocumentServiceImpl documentService;

    @Autowired
    public ReportsController(DocumentServiceImpl documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/uploadDocument", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Document> uploadDocument(@RequestPart MultipartFile file, @RequestParam boolean save) {
        Document document;
        if (save) {
            document = documentService.readAndSaveDocument(file);
        } else {
            document = documentService.readDocument(file);
        }
        LOGGER.info("File: {}", document.toString());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(document);
    }
}
