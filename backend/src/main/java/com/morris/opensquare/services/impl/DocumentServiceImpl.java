package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.FileType;
import com.morris.opensquare.models.documents.Document;
import com.morris.opensquare.repositories.DocumentRepository;
import com.morris.opensquare.services.DocumentService;
import com.morris.opensquare.utils.FileUtil;
import org.bson.BsonBinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document readDocument(MultipartFile multipartFile) {
        BsonBinary base64BsonBinary = FileUtil.base64ToBsonBinary(multipartFile);
        String[] fileTypeParts = Objects.requireNonNull(multipartFile.getContentType()).split("\\.");
        String fileType = fileTypeParts[fileTypeParts.length - 1];
        return Document.builder()
                .fileName(multipartFile.getName())
                .fileSize(multipartFile.getSize())
                .binary(base64BsonBinary)
                .fileType(FileType.valueOf(fileType))
                .build();
    }

    @Override
    public Document readAndSaveDocument(MultipartFile multipartFile) {
        Document document = readDocument(multipartFile);
        return documentRepository.save(document);
    }
}
