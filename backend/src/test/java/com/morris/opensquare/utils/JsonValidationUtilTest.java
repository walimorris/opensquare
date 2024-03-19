package com.morris.opensquare.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.morris.opensquare.TestHelper;
import com.morris.opensquare.models.youtube.YouTubeVideo;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles({"coverage"})
class JsonValidationUtilTest {
    private static YouTubeVideo youtubeVideo;
    private static final String YOUTUBE_VIDEO_1 = "backend/src/test/resources/youtube/YouTubeVideo_1.json";
    private static final String YOUTUBE_VIDEO_JSON_VALIDATION_SCHEMA = "backend/src/test/resources/schemas/YouTubeVideo.json";

    @BeforeEach
    void setUp() throws IOException {
        youtubeVideo = (YouTubeVideo) TestHelper.convertModelFromFile(YOUTUBE_VIDEO_1, YouTubeVideo.class, null);
    }

    @Test
    void isValidJsonSchema() throws IOException, JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        JSONObject o = new JSONObject(objectMapper.writeValueAsString(youtubeVideo));

        Set<ValidationMessage> errors = null;
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        try (InputStream schemaStream = inputStreamFromSchemaPath(YOUTUBE_VIDEO_JSON_VALIDATION_SCHEMA)) {
            if (schemaStream != null) {
                JsonSchema schema = factory.getSchema(schemaStream);
                JsonNode jsonNode = objectMapper.readTree(inputStreamFromJSONObject(o));
                errors = schema.validate(jsonNode);
            }
        }
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
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