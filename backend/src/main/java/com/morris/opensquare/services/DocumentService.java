package com.morris.opensquare.services;

import com.morris.opensquare.models.documents.Document;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface DocumentService {

    /**
     * Read a {@link MultipartFile} and create a {@link Document} from it.
     *
     * @param principal authorized user
     * @param multipartFile {@link MultipartFile}
     *
     * @return {@link Document}
     */
    Document readDocument(Principal principal, MultipartFile multipartFile);

    /**
     * Read a {@link MultipartFile}, create a {@link Document} from it and
     * persist the document.
     *
     * @param principal authorized user
     * @param multipartFile {@link MultipartFile}
     * @return {@link Document}
     */
    Document readAndSaveDocument(Principal principal, MultipartFile multipartFile);

    /**
     * Get all documents for user.
     *
     * @param principal {@link Principal}
     * @return {@link List}
     */
    List<Document> getAllDocumentsForUser(Principal principal);

    /**
     * Get document by supplying document object id and userId of document owner.
     *
     * @param principal {@link Principal} userId of document owner
     * @param documentId {@link ObjectId} documentId
     *
     * @return {@link Document}
     */
    Document getDocumentByDocumentIdAndUserId(Principal principal, ObjectId documentId);
}
