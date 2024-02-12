package com.morris.opensquare.services

import org.bson.BsonDocument
import org.bson.BsonInt64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

@Service
class AtlasFunctionService @Autowired constructor(val mongoTemplate: MongoTemplate){

    fun func() {
        val command = BsonDocument("dbStats", BsonInt64(1))
        val result = mongoTemplate.db.runCommand(command)
        println("Function from AtlasFunction: kotlin")
        println("dbstats: " + result.toJson())
    }
}