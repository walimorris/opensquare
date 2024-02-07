package com.morris.opensquare.models.digitalfootprints;

import com.morris.opensquare.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles({"coverage"})
class NSLookupFootPrintTest {

    private static final String NAME_1 = "domain1";
    private static final String NAME_2 = "domain2";
    private static final String ADDRESS_1 = "127.0.0.1";
    private static final String ADDRESS_2 = "127.0.0.2";

    private static final String NSLOOKUPFOOTPRINT_FILE = "backend/src/test/resources/models/NSLookupFootPrint.json";

    private static NSLookupFootPrint nsLookupFootPrint;

    @BeforeEach
    void setUp() throws IOException {
        nsLookupFootPrint = (NSLookupFootPrint) TestHelper.convertModelFromFile(NSLOOKUPFOOTPRINT_FILE, NSLookupFootPrint.class, null);
    }

    @Test
    void getName() {
        assertEquals(NAME_1, nsLookupFootPrint.getName());
    }

    @Test
    void setName() {
        nsLookupFootPrint.setName(NAME_2);
        assertNotEquals(NAME_1, nsLookupFootPrint.getName());
        assertEquals(NAME_2, nsLookupFootPrint.getName());
    }

    @Test
    void getAddress() {
        assertEquals(ADDRESS_1, nsLookupFootPrint.getAddress());
    }

    @Test
    void setAddress() {
        nsLookupFootPrint.setAddress(ADDRESS_2);
        assertNotEquals(ADDRESS_1, nsLookupFootPrint.getAddress());
        assertEquals(ADDRESS_2, nsLookupFootPrint.getAddress());
    }
}
