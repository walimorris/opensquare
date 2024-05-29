package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.documents.PdfDocument;
import com.morris.opensquare.services.DocumentService;
import com.morris.opensquare.utils.FileUtil;
import org.bson.BsonBinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Override
    public PdfDocument readPdfDocument(MultipartFile multipartFile) {
        BsonBinary base64BsonBinary = FileUtil.base64ToBsonBinary(multipartFile);
        return PdfDocument.builder()
                .fileName(multipartFile.getName())
                .fileSize(multipartFile.getSize())
                .binary(base64BsonBinary)
                .build();
    }
}
