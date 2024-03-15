package com.morris.opensquare.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.morris.opensquare.TestHelper;
import com.morris.opensquare.models.youtube.YouTubeVideo;
import com.morris.opensquare.services.loggers.LoggerService;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

@SpringBootTest
@ActiveProfiles({"coverage"})
class JsonValidationUtilTest {

    @Autowired
    private JsonValidationUtil jsonValidationUtil;

    @MockBean
    private LoggerService loggerService;
    private static YouTubeVideo youtubeVideo;
    private static final String YOUTUBE_VIDEO_1 = "backend/src/test/resources/youtube/YouTubeVideo_2.json";
    private static final String YOUTUBE_VIDEO_JSON_VALIDATION_SCHEMA = "backend/src/main/resources/schemas/YouTubeVideo.json";

    @BeforeEach
    void setUp() throws IOException {
        youtubeVideo = (YouTubeVideo) TestHelper.convertModelFromFile(YOUTUBE_VIDEO_1, YouTubeVideo.class, null);
        jsonValidationUtil = new JsonValidationUtil(loggerService);
    }

    @Test
    void isValidJsonSchema() throws IOException, JSONException {
        // create date
        Date date = new Date();
        ObjectId objectIdDate = new ObjectId(date);

        // create publishDate
        String str = "1986-04-08T12:30:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        // create thumbnail
        String thumbnail = "http://www.thumby.com";
        youtubeVideo.setId(objectIdDate);
        youtubeVideo.setPublishDate(dateTime);
        youtubeVideo.setThumbnail(thumbnail);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        JSONObject o = new JSONObject(objectMapper.writeValueAsString(youtubeVideo));

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema jsonSchema = factory.getSchema("#YouTubeVideo.json");
        JsonNode jsonNode = objectMapper.readTree(new ByteArrayInputStream(o.toString().getBytes()));
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        System.out.println(errors.size());
    }
}