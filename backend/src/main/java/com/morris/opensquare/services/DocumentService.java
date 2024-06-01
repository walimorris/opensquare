package com.morris.opensquare.services;

import com.morris.opensquare.models.documents.Document;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    /**
     * Read a {@link MultipartFile} and create a {@link Document} from it.
     *
     * @param multipartFile {@link MultipartFile}
     * @return {@link Document}
     */
    Document readDocument(MultipartFile multipartFile);

    /**
     * Read a {@link MultipartFile}, create a {@link Document} from it and
     * persist the document.
     *
     * @param multipartFile {@link MultipartFile}
     * @return {@link Document}
     */
    Document readAndSaveDocument(MultipartFile multipartFile);
}
