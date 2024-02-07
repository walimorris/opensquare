package com.morris.opensquare.models.digitalfootprints;

import com.morris.opensquare.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"coverage"})
class NSLookupFootPrintListTest {

    private static final String NAME_1 = "domain1";
    private static final String NAME_2 = "domain2";
    private static final String ADDRESS_1 = "127.0.0.1";
    private static final String ADDRESS_2 = "127.0.0.2";
    private static final String NSLOOKUPFOOTPRINT_LIST_FILE = "backend/src/test/resources/models/NSLookupFootPrintList.json";

    private static List<NSLookupFootPrint> nsLookupFootPrints;
    private static NSLookupFootPrintList nsLookupFootPrintList;

    @BeforeEach
    @SuppressWarnings("unchecked cast")
    void setUp() throws IOException {
        nsLookupFootPrints = (List<NSLookupFootPrint>) TestHelper.convertModelFromFile(NSLOOKUPFOOTPRINT_LIST_FILE, List.class, NSLookupFootPrint.class);
        nsLookupFootPrintList =  new NSLookupFootPrintList(nsLookupFootPrints);
    }

    @Test
    void getNsLookupFootPrintList() {
        List<NSLookupFootPrint> footPrintList = nsLookupFootPrintList.getNsLookupFootPrintList();
        assertNotNull(nsLookupFootPrintList.getNsLookupFootPrintList());
        NSLookupFootPrint footPrint1 = footPrintList.get(0);
        NSLookupFootPrint footPrint2 = footPrintList.get(1);
        assertAll(
                () -> assertNotNull(footPrintList),
                () -> assertEquals(NAME_1, footPrint1.getName()),
                () -> assertEquals(ADDRESS_1, footPrint1.getAddress()),
                () -> assertEquals(NAME_2, footPrint2.getName()),
                () -> assertEquals(ADDRESS_2, footPrint2.getAddress())
        );
    }
}
