package com.morris.opensquare.repositories;

import com.morris.opensquare.models.loggers.MongoLogger;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoLoggerRepository extends MongoRepository<MongoLogger, String> {
}
