package com.morris.opensquare.services.impl;

import com.morris.opensquare.services.FileService;
import com.morris.opensquare.services.loggers.LoggerService;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    private final LoggerService loggerService;

    @Autowired
    public FileServiceImpl(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @Override
    public File getFile(@NonNull String fileName) {
        return new File(fileName);
    }

    @Override
    public File writeJsonToFile(@NonNull JSONArray json, @NonNull String fileName) {
        File localFile = null;

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json.toString(4));
            localFile = new File(fileName);
        } catch (IOException e) {
            loggerService.saveLog(e.getClass().getName(), "Failed to write json to file: " + e.getMessage(), Optional.of(LOGGER));
        }
        return localFile;
    }

    @Override
    public BufferedReader processBufferedReader(@NonNull String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            return new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e) {
            loggerService.saveLog(e.getClass().getName(), e.getMessage(), Optional.of(LOGGER));
        }
        return null;
    }
}
