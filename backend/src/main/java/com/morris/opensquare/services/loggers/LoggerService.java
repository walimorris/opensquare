package com.morris.opensquare.services.loggers;

import org.slf4j.Logger;

import java.util.Optional;

public interface LoggerService {

    /**
     * Saves and persists log to storage unit.
     *
     * @param module log module (com.example.class)
     * @param message log message
     * @param errorLogger {@link Optional<Logger>} meant for error logs
     */
    void saveLog(String module, String message, Optional<Logger> errorLogger);
}
