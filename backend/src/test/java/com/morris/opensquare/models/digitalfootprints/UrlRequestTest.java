package com.morris.opensquare.models.digitalfootprints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UrlRequestTest {

    private static final String URL_1 = "https://google.com";
    private static final String URL_2 = "https://amazon.com";

    private UrlRequest urlRequest;

    @BeforeEach
    void setUp() {
        urlRequest = getUrlRequest();
    }

    @Test
    void getUrl() {
        assertAll(
                () -> assertNotNull(urlRequest),
                () -> assertEquals(URL_1, urlRequest.getUrl())
        );
    }

    @Test
    void setUrl() {
        urlRequest.setUrl(URL_2);
        assertAll(
                () -> assertNotNull(urlRequest),
                () -> assertNotEquals(URL_1, urlRequest.getUrl()),
                () -> assertEquals(URL_2, urlRequest.getUrl())
        );
    }

    private static UrlRequest getUrlRequest() {
        return new UrlRequest(URL_1);
    }
}
