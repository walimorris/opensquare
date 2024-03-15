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

import java.io.*;
import java.util.Optional;
import java.util.Set;

@Component("JsonValidationUtil")
public class JsonValidationUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonValidationUtil.class);

    private final LoggerService loggerService;

    @Autowired
    public JsonValidationUtil(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    /**
     * Ensures that any given object is valid against a given schema validation model.
     *
     * @param schemaPath {@link String} path to schema validation model
     * @param o {@link Object} The object to validate
     * @param clazz {@link Class} the object's class
     *
     * @return boolean - if the object is valid or not valid
     */
    public boolean isValidJsonSchema(String schemaPath, Object o, Class<?> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JSONObject jsonObject = null;
        try {
            if (clazz == YouTubeVideo.class) {
                jsonObject = new JSONObject(objectMapper.writeValueAsString(o));
            }
        } catch (JsonProcessingException e) {
            loggerService.saveLog(e.getClass().getName(), e.getMessage(), Optional.of(LOGGER));
        }

        if (jsonObject != null) {
            JsonSchemaFactory validatorFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

            try (InputStream schemaStream = inputStreamFromSchemaPath(schemaPath)) {
                JsonSchema schema = validatorFactory.getSchema(schemaStream);
                JsonNode objectJsonFinal = objectMapper.readTree(inputStreamFromJSONObject(jsonObject));
                Set<ValidationMessage> validationResult = schema.validate(objectJsonFinal);
                if (validationResult.isEmpty()) {
                    return true;
                } else {
                    LOGGER.info("Validation Results on object class: {}", o.getClass());
                    validationResult.forEach(vm -> LOGGER.info(vm.getMessage()));
                    return false;
                }
            } catch (IOException e) {
                loggerService.saveLog(e.getClass().getName(), e.getMessage(), Optional.of(LOGGER));
            }
        }
        return false;
    }

    private InputStream inputStreamFromSchemaPath(String path) throws FileNotFoundException {
        if (found(path)) {
            String p = resolvePath(path);
            File resolvedFile = new File(p);
            return new FileInputStream(resolvedFile);
        }
        return null;
    }

    private InputStream inputStreamFromJSONObject(JSONObject jsonObject) {
        return new ByteArrayInputStream(jsonObject.toString().getBytes());
    }

    private String resolvePath(String filename) {
        File file = new File(filename);
        return  file.getAbsolutePath();
    }

    private boolean found(String path) {
        String p = resolvePath(path);
        File file = new File(p);
        return file.exists();
    }
}


