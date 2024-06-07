package com.morris.opensquare.utils;

import org.bson.BsonBinary;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;
import java.util.List;

public class FileUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Get a {@link File} from given file name.
     *
     * @param fileName {@link String} file to get
     * @return {@link File}
     */
    public static File getFile(@NonNull String fileName) {
        return new File(fileName);
    }

    /**
     * Writes {@link JSONArray} object to given file.
     *
     * @param json {@link JSONArray} proposed json array object written to file
     * @param fileName {@link String} file name to write object
     *
     * @return {@link File} the written file
     */
    public static File writeJsonToFile(@NonNull JSONArray json, @NonNull String fileName) {
        File localFile = null;

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json.toString(4));
            localFile = new File(fileName);
        } catch (IOException e) {
            LOGGER.error("Failed to write json to file: {}", e.getMessage());
        }
        return localFile;
    }

    /**
     * Get BufferedReader from process execution.
     *
     * @param command {@link List} process command
     * @return {@link BufferedReader}
     */
    public static BufferedReader processBufferedReader(@NonNull List<String> command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            return new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e) {
            LOGGER.error("Error reading command: {}", e.getMessage());
        }
        return null;
    }

    public static String base64partEncodedStr(Object binary) {
        String encodedStr = null;
        byte[] sourceBytes;
        try {
            if (binary instanceof MultipartFile file) {
                sourceBytes = Base64.getEncoder().encode(file.getBytes());
                String sourceStr = new String(sourceBytes);
                encodedStr = "data:" + file.getContentType() + ";base64," + sourceStr;
            } else {
                if (binary instanceof BsonBinary bsonBinary) {
                    sourceBytes = bsonBinary.getData();
                    String sourceStr = new String(sourceBytes);
                    encodedStr = "data:" + ((BsonBinary) binary).getType() + ";base64," + sourceStr;
                }
            }
        } catch (IOException e) {
            LOGGER.info("Error converting Multipart File to Base64 encoded String: {}", e.getMessage());
        }
        return encodedStr;
    }

    public static BsonBinary base64ToBsonBinary(MultipartFile file) {
        BsonBinary bsonBinary = null;
        try {
            byte[] sourceBytes = Base64.getEncoder().encode(file.getBytes());
            bsonBinary = new BsonBinary(sourceBytes);
        } catch (IOException e) {
            LOGGER.info("Error converting Multipart File to Base64 encoded String: {}", e.getMessage());
        }
        return bsonBinary;
    }
}
