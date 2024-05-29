package com.morris.opensquare.utils;

import org.bson.BsonBinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Component("fileUtil")
public class FileUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

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
