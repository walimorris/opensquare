package com.morris.opensquare.services.impl;

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.morris.opensquare.services.WatchStreamService;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchStreamServiceImpl implements WatchStreamService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WatchStreamServiceImpl.class);

    private static final String DATABASE = "sample_restaurants";
    private static final String DISPOSABLE_EMAIL_DOMAINS_COLLECTION = "disposable_email_domains";
    private static final String MONGO_LOGGER_COLLECTION = "mongo_logger";
    private static final String GLOBAL_NOTIFICATIONS_COLLECTION = "global_notifications";
    private static final String INSERT = "insert";
    private static final String UPDATE = "update";
    private static final String MODIFY = "modify";
    private static final String OP_TYPE = "operationType";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public WatchStreamServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void watchEmailDomainChangeStream() {
        MongoCollection<Document> collection = getCollection(DISPOSABLE_EMAIL_DOMAINS_COLLECTION);
        List<Bson> pipeline = insertUpdateModifyOperationsPipeline();
        watchCollectionChangeStream(collection, pipeline);
    }

    @Override
    public void watchMongoLoggerChangeStream() {
        MongoCollection<Document> collection = getCollection(MONGO_LOGGER_COLLECTION);
        List<Bson> pipeline = insertUpdateModifyOperationsPipeline();
        watchCollectionChangeStream(collection, pipeline);
    }

    @Override
    public void watchGlobalNotificationsChangeStream() {
        MongoCollection<Document> collection = getCollection(GLOBAL_NOTIFICATIONS_COLLECTION);
        List<Bson> pipeline = insertUpdateModifyOperationsPipeline();
        watchCollectionChangeStream(collection, pipeline);
    }

    @Override
    public void watchall() {
        watchEmailDomainChangeStream();
        watchMongoLoggerChangeStream();
        watchGlobalNotificationsChangeStream();
    }

    private MongoDatabase getDatabaseInstance() {
        return mongoTemplate.getMongoDatabaseFactory().getMongoDatabase(DATABASE);
    }

    private MongoCollection<Document> getCollection(String collection) {
        return getDatabaseInstance().getCollection(collection);
    }

    private List<Bson> insertUpdateModifyOperationsPipeline() {
        List<String> operations = List.of(INSERT, UPDATE, MODIFY);
        return List.of(Aggregates.match(Filters.in(OP_TYPE, operations)));
    }

    private void watchCollectionChangeStream(MongoCollection<Document> collection, List<Bson> pipeline) {
        ChangeStreamIterable<Document> changeStream = collection.watch(pipeline).fullDocument(FullDocument.DEFAULT);
        for (ChangeStreamDocument<Document> document : changeStream) {
            LOGGER.info("Change Stream Document: {}", document);
        }
    }
}
