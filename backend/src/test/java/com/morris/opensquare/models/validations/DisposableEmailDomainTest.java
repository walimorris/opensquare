package com.morris.opensquare.models.validations;

import com.morris.opensquare.TestHelper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles({"coverage"})
class DisposableEmailDomainTest {
    private static final String DOMAIN_NAME_1 = "disposableDomain.net";
    private static final String DOMAIN_NAME_2 = "anotherDisposableDomain.net";
    private static final int TIMESTAMP = 123456789;
    private static final String DISPOSABLE_DOMAIN_FILE = "backend/src/test/resources/models/DisposableEmailDomain.json";

    private static DisposableEmailDomain disposableEmailDomain;

    @BeforeEach
    void setUp() throws IOException {
        disposableEmailDomain = (DisposableEmailDomain) TestHelper.convertModelFromFile(DISPOSABLE_DOMAIN_FILE, DisposableEmailDomain.class, null);
    }

    @Test
    void getDomainName() {
        assertEquals(DOMAIN_NAME_1, disposableEmailDomain.getDomainName());
    }

    @Test
    void getId() {
        // We can ensure the time stamp from the mock DisposableEmailDomain is correct
        // from thr ObjectId
        assertEquals(TIMESTAMP, disposableEmailDomain.getId().getTimestamp());
    }

    @Test
    void setDomainName() {
        disposableEmailDomain.setDomainName(DOMAIN_NAME_2);
        assertNotEquals(DOMAIN_NAME_1, disposableEmailDomain.getDomainName());
        assertEquals(DOMAIN_NAME_2, disposableEmailDomain.getDomainName());
    }

    @Test
    void setId() {
        ObjectId testId = new ObjectId();
        disposableEmailDomain.setId(testId);
        assertNotEquals(TIMESTAMP, disposableEmailDomain.getId().getTimestamp());
        assertEquals(testId.getTimestamp(), disposableEmailDomain.getId().getTimestamp());
        assertEquals(testId, disposableEmailDomain.getId());
    }
}