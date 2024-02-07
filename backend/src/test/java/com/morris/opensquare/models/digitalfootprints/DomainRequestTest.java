package com.morris.opensquare.models.digitalfootprints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DomainRequestTest {

    private static final String DOMAIN_1 = "google.com";
    private static final String DOMAIN_2 = "amazon.com";

    private DomainRequest domainRequest;

    @BeforeEach
    void setUp() {
        domainRequest = getDomainRequest();
    }

    @Test
    void getDomain() {
        assertAll(
                () -> assertNotNull(domainRequest),
                () -> assertEquals(DOMAIN_1, domainRequest.getDomain())
        );
    }

    @Test
    void setDomain() {
        domainRequest.setDomain(DOMAIN_2);
        assertAll(
                () -> assertNotNull(domainRequest),
                () -> assertNotEquals(DOMAIN_1, domainRequest.getDomain()),
                () -> assertEquals(DOMAIN_2, domainRequest.getDomain())

        );
    }

    private DomainRequest getDomainRequest() {
        return new DomainRequest(DOMAIN_1);
    }
}
