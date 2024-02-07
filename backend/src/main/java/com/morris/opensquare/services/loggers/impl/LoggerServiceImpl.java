package com.morris.opensquare.services.loggers.impl;

import com.morris.opensquare.models.loggers.MongoLogger;
import com.morris.opensquare.repositories.MongoLoggerRepository;
import com.morris.opensquare.services.loggers.LoggerService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import static com.morris.opensquare.utils.Constants.*;

@Service
public class LoggerServiceImpl implements LoggerService {

    private final MongoLoggerRepository mongoLoggerRepository;

    @Autowired
    public LoggerServiceImpl(final MongoLoggerRepository mongoLoggerRepository) {
        this.mongoLoggerRepository = mongoLoggerRepository;
    }

    @Override
    public void saveLog(String module, String message, Optional<Logger> errorLogger) {
        if (!stackWalkerPeekIfTestEngine()) {
            String createdAt = pleaseGetTimestampNow();
            ObjectId id = new ObjectId();
            message = message.trim();

            MongoLogger log = new MongoLogger.Builder()
                    .id(id)
                    .module(module)
                    .message(message)
                    .createdAt(createdAt)
                    .build();

            if (errorLogger.isPresent()) {
                errorLogger.get().error(message);
            }

            mongoLoggerRepository.save(log);
        }
    }

    private boolean stackWalkerPeekIfTestEngine() {
        Optional<StackWalker.StackFrame> result = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(stack -> stack.filter(stackFrame -> stackFrame
                                .getClassName()
                                .contains(TEST_ENGINE))
                        .findFirst());

        return result.map(stackFrame -> stackFrame
                        .getClassName()
                        .contains(TEST_ENGINE))
                .orElse(false);
    }

    private String pleaseGetTimestampNow() {
        DateFormat dateFormat = new SimpleDateFormat(CASSANDRA_TIMESTAMP_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of(PST)));
        return dateFormat.format(new Date());
    }
}
