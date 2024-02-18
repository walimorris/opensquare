package com.morris.opensquare.models.Notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.morris.opensquare.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.morris.opensquare.utils.Constants.OPENSQUARE_JAVA_MONGODB_TIME_PATTERN;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"coverage"})
class OwaspBlogReferenceTest {

    @Configuration
    static class ContextConfiguration {
        // TODO: Add more url test cases

        // this bean will be injected into this test class
        @Bean
        public ObjectMapper objectMapper() {
            return JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();
        }
    }

    // object 1
    private static final String DATE_1 = "2024-02-08T08:00:00.000+00:00";
    private static final String AUTHOR_1 = "John Sotiropoulos";
    private static final String TITLE_1 = "2024-02-08-OWASP-joins-NIST-AISIC-launch-announcment_post.";
    private static final String URL_1 = "https://wwww.fakeurl.net";
    private static final String OWASP_BLOG_REFERENCE_FILE = "backend/src/test/resources/models/OwaspBlogReference.json";
    private static OwaspBlogReference owaspBlogReference;

    // object 2
    private static final String DATE_2 = "2024-04-22T08:00:00.000+00:00";
    private static final String AUTHOR_2 = "Albert Gonzalez";
    private static final String TITLE_2 = "Best Ways to Protect your Credit Card Info";
    private static final String URL_2 = "https://wwww.totallyfakeurl.org";

    // builder object
    private static final String URL_RESULT = "https://owasp.org/blog/2024/02/08/OWASP-joins-NIST-AISIC-launch-announcment_post.html";

    @BeforeEach
    void setUp() throws IOException {
        owaspBlogReference = (OwaspBlogReference) TestHelper.convertModelFromFile(OWASP_BLOG_REFERENCE_FILE, OwaspBlogReference.class, null);
    }
    @Test
    void getDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(OPENSQUARE_JAVA_MONGODB_TIME_PATTERN);
        LocalDateTime date = LocalDateTime.parse(DATE_1, dateTimeFormatter);
        assertEquals(date, owaspBlogReference.getDate());
    }

    @Test
    void setDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(OPENSQUARE_JAVA_MONGODB_TIME_PATTERN);
        LocalDateTime originalDate = owaspBlogReference.getDate();
        LocalDateTime setDate = LocalDateTime.parse(DATE_2, dateTimeFormatter);
        assertNotEquals(originalDate, setDate);
    }

    @Test
    void getAuthor() {
        assertEquals(AUTHOR_1, owaspBlogReference.getAuthor());
    }

    @Test
    void setAuthor() {
        owaspBlogReference.setAuthor(AUTHOR_2);
        assertNotEquals(AUTHOR_1, owaspBlogReference.getAuthor());
    }

    @Test
    void getTitle() {
        assertEquals(TITLE_1, owaspBlogReference.getTitle());
    }

    @Test
    void setTitle() {
        owaspBlogReference.setTitle(TITLE_2);
        assertNotEquals(TITLE_1, owaspBlogReference.getTitle());
    }

    @Test
    void getUrl() {
        assertEquals(URL_1, owaspBlogReference.getUrl());
    }

    @Test
    void setUrl() {
        owaspBlogReference.setUrl(URL_2);
        assertNotEquals(URL_1, owaspBlogReference.getUrl());
    }

    @Test
    void build() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(OPENSQUARE_JAVA_MONGODB_TIME_PATTERN);
        LocalDateTime date = LocalDateTime.parse(DATE_2, dateTimeFormatter);

        OwaspBlogReference owaspBlogReferenceBuilder = new OwaspBlogReference.Builder()
                .date(date)
                .author(AUTHOR_2)
                .title(TITLE_2)
                .url(URL_2)
                .build();

        assertAll(
                () -> assertEquals(date, owaspBlogReferenceBuilder.getDate()),
                () -> assertEquals(AUTHOR_2, owaspBlogReferenceBuilder.getAuthor()),
                () -> assertEquals(TITLE_2, owaspBlogReferenceBuilder.getTitle()),
                () -> assertEquals(URL_2, owaspBlogReferenceBuilder.getUrl())
        );
    }

    @Test
    void buildWithoutUrl() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(OPENSQUARE_JAVA_MONGODB_TIME_PATTERN);
        LocalDateTime date = LocalDateTime.parse(DATE_1, dateTimeFormatter);

        OwaspBlogReference owaspBlogReferenceBuilder = new OwaspBlogReference.Builder()
                .date(date)
                .author(AUTHOR_1)
                .title(TITLE_1)
                .build();

        assertAll(
                () -> assertEquals(date, owaspBlogReferenceBuilder.getDate()),
                () -> assertEquals(AUTHOR_1, owaspBlogReferenceBuilder.getAuthor()),
                () -> assertEquals(TITLE_1, owaspBlogReferenceBuilder.getTitle()),
                () -> assertEquals(URL_RESULT, owaspBlogReferenceBuilder.getUrl())
        );
    }
}