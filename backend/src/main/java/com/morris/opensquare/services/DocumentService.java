package com.morris.opensquare.services;

import com.morris.opensquare.models.documents.PdfDocument;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    PdfDocument readPdfDocument(MultipartFile multipartFile);
}
