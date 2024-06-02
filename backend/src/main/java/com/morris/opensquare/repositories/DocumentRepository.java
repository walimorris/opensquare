package com.morris.opensquare.repositories;

import com.morris.opensquare.models.documents.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DocumentRepository extends MongoRepository<Document, String> {

    @Query("{userId :  '?0'}")
    List<Document> getDocumentsByUserId(String userId);

    @Query("{id: '?0', userId:  '?1'}")
    Document getDocumentByDocumentIdAndUserId(ObjectId id, String userId);
}
