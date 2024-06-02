package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.FileType;
import com.morris.opensquare.models.documents.Document;
import com.morris.opensquare.repositories.DocumentRepository;
import com.morris.opensquare.services.DocumentService;
import com.morris.opensquare.utils.FileUtil;
import org.bson.BsonBinary;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
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
    public Document readDocument(Principal principal, MultipartFile multipartFile) {
        // TODO: handle duplications
        BsonBinary base64BsonBinary = FileUtil.base64ToBsonBinary(multipartFile);
        String[] fileTypeParts = Objects.requireNonNull(multipartFile.getContentType()).split("\\.");
        String fileType = fileTypeParts[fileTypeParts.length - 1];
        return Document.builder()
                .fileName(multipartFile.getName())
                .userId(principal.getName())
                .fileSize(multipartFile.getSize())
                .binary(base64BsonBinary)
                .fileType(FileType.valueOf(fileType))
                .build();
    }

    @Override
    public Document readAndSaveDocument(Principal principal, MultipartFile multipartFile) {
        Document document = readDocument(principal, multipartFile);
        return documentRepository.save(document);
    }

    @Override
    public List<Document> getAllDocumentsForUser(Principal principal) {
        return documentRepository.getDocumentsByUserId(principal.getName());
    }

    @Override
    public Document getDocumentByDocumentIdAndUserId(Principal principal, ObjectId documentId) {
        return documentRepository.getDocumentByDocumentIdAndUserId(documentId, principal.getName());
    }
}
