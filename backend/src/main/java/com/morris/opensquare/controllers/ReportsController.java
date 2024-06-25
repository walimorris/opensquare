package com.morris.opensquare.controllers;

import com.morris.opensquare.models.DocumentUploadRequest;
import com.morris.opensquare.models.documents.Document;
import com.morris.opensquare.services.impl.DocumentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.Base64;

@RestController
@RequestMapping("/opensquare/api/reports")
public class ReportsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsController.class);

    private final DocumentServiceImpl documentService;

    @Autowired
    public ReportsController(DocumentServiceImpl documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/uploadDocument")
    public ResponseEntity<Document> uploadDocument(Principal principal, @RequestBody DocumentUploadRequest request) {

        try {
            // Decode the Base64 file content
            byte[] decodedBytes = Base64.getDecoder().decode(request.getFile());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);
            MultipartFile multipartFile = new MockMultipartFile(request.getFilename(), byteArrayInputStream);

            Document document;
            if (request.isSave()) {
                document = documentService.readAndSaveDocument(principal, multipartFile);
            } else {
                document = documentService.readDocument(principal, multipartFile);
            }
            LOGGER.info("File: {}", document.toString());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(document);
        } catch (Exception e) {
            LOGGER.error("Error processing file upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
