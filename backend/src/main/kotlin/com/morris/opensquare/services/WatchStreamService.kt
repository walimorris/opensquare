package com.morris.opensquare.services

import com.mongodb.client.ChangeStreamIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.changestream.FullDocument
import org.bson.Document
import org.bson.conversions.Bson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

/**
 * The WatchStreamService is a convenience service that allows opensquare application to monitor
 * change data capture (CDC) events for important data changes. This CDC service is extensible
 * and can be used to propagate changes to downstream data sinks, update UI, or trigger in-app events.
 *
 * @see ChangeStreamIterable
 */
@Service
class WatchStreamService @Autowired constructor(private val mongoTemplate: MongoTemplate) {
    companion object {
        private const val DATABASE = "sample_restaurants"
        private const val DISPOSABLE_EMAIL_DOMAINS_COLLECTION = "disposable_email_domains"
        private const val MONGO_LOGGER_COLLECTION = "mongo_logger"
        private const val INSERT = "insert"
        private const val UPDATE = "update"
        private const val OP_TYPE = "operationType"
    }

    fun watchEmailDomainChangeStream() {
        val collection = getDisposableEmailDomainCollection()
        val pipeline: MutableList<Bson> = insertUpdateOperationsPipeline()
        watchCollectionChangeStream(collection, pipeline)
    }

    fun watchMongoLoggerChangeStream() {
        val collection = getMongoLoggerCollection()
        val pipeline: MutableList<Bson> = insertUpdateOperationsPipeline()
        watchCollectionChangeStream(collection, pipeline)
    }

    fun watchAll() {
        watchEmailDomainChangeStream()
        watchMongoLoggerChangeStream()
    }

    private fun getDatabaseInstance(): MongoDatabase {
        return mongoTemplate.mongoDatabaseFactory.getMongoDatabase(DATABASE)
    }

    private fun getDisposableEmailDomainCollection(): MongoCollection<Document> {
        return getDatabaseInstance().getCollection(DISPOSABLE_EMAIL_DOMAINS_COLLECTION)
    }

    private fun getMongoLoggerCollection(): MongoCollection<Document> {
        return getDatabaseInstance().getCollection(MONGO_LOGGER_COLLECTION)
    }

    private fun insertUpdateOperationsPipeline(): MutableList<Bson> {
        val operations: List<String> = arrayListOf(INSERT, UPDATE)
        return mutableListOf(Aggregates.match(Filters.`in`(OP_TYPE, operations)))
    }

    private fun watchCollectionChangeStream(collection: MongoCollection<Document>, pipeline: MutableList<Bson>) {
        val changeStream: ChangeStreamIterable<Document> = collection.watch(pipeline).fullDocument(FullDocument.UPDATE_LOOKUP)
        for (document in changeStream) {
            println(document)
        }
    }
}