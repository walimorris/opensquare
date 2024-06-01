package com.morris.opensquare.repositories;

import com.morris.opensquare.models.documents.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<Document, String> {
}
