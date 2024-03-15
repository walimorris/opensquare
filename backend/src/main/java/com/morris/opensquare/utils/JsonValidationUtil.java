package com.morris.opensquare.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.morris.opensquare.models.youtube.YouTubeVideo;
import com.morris.opensquare.services.loggers.LoggerService;
import com.networknt.schema.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Component("JsonValidationUtil")
public class JsonValidationUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonValidationUtil.class);

    private final LoggerService loggerService;

    @Autowired
    public JsonValidationUtil(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public boolean isValidJsonSchema(String schemaPath, Object o, Class<?> clazz) {
        System.out.println(resolvePath(schemaPath));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JSONObject jsonObject = null;
        try {
            if (clazz == YouTubeVideo.class) {
                jsonObject = new JSONObject(objectMapper.writeValueAsString(o));
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        if (jsonObject != null) {
            System.out.println(jsonObject.toString());
            try (InputStream jsonStream = inputStreamFromJSONObject(jsonObject)) {
                JsonNode json = objectMapper.readTree(jsonStream);
                JsonSchemaFactory validatorFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

                try (InputStream schemaStream = inputStreamFromSchemaPath(resolvePath(schemaPath))) {
                    JsonSchema schema = validatorFactory.getSchema(schemaStream);
                    Set<ValidationMessage> validationResult = schema.validate(json);
                    if (validationResult.isEmpty()) {
                        System.out.println("no validation errors :-)");
                        return true;
                    } else {
                        System.out.println("validation result size: " + validationResult.size());
                        validationResult.forEach(vm -> System.out.println(vm.getMessage()));
                    }
                }
            } catch (IOException e) {
                System.out.println("Some Error occurred validating schema= " + schemaPath + ": " + e.getMessage());
            }
        }
        return false;
    }

    private InputStream inputStreamFromSchemaPath(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    private InputStream inputStreamFromJSONObject(JSONObject jsonObject) {
        return new ByteArrayInputStream(jsonObject.toString().getBytes());
    }

    private String resolvePath(String filename) {
        File file = new File(filename);
        return  file.getAbsolutePath();
    }
}


