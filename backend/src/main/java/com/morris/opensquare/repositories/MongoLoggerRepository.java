package com.morris.opensquare.repositories;

import com.morris.opensquare.models.loggers.MongoLogger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoLoggerRepository extends MongoRepository<MongoLogger, String> {
}
