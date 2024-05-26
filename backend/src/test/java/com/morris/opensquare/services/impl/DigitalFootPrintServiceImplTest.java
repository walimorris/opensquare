package com.morris.opensquare.services.impl;

import com.morris.opensquare.TestHelper;
import com.morris.opensquare.models.digitalfootprints.NSLookupFootPrintList;
import com.morris.opensquare.models.digitalfootprints.OS;
import com.morris.opensquare.models.digitalfootprints.WhoIsFootPrint;
import com.morris.opensquare.services.DigitalFootPrintService;
import com.morris.opensquare.services.FileService;
import com.morris.opensquare.services.IdentityGenerator;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static com.morris.opensquare.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"coverage"})
class DigitalFootPrintServiceImplTest {

    @Autowired
    DigitalFootPrintService digitalFootPrintService;

    @MockBean
    FileService fileService;

    @MockBean
    IdentityGenerator identityGenerator;

    private static final String GOOGLE_DOMAIN = "google.com";
    private static final String YAHOO_DOMAIN = "yahoo.com";
    private static final String VKONTAKTE_DOMAIN = "vk.com";
    private static final String VALID_URL_1 = "https://www.github.com";
    private static final String VALID_URL_2 = "https://github.com";
    private static final String INVALID_URL_1 = "www.github.com";
    private static final String INVALID_URL_2 = "github.com";
    private static final String TEXT_WITH_URLS_1 = "Many people visit https://google.com everyday. google.com is a great search service" +
            "but www.askjeeves.com was a very popular search engine in the early 2000s, just like www.yahoo or http://yandex.com";

    private static final String GOOGLE_WHOIS_RESULT = "backend/src/test/resources/digital/google-whois.txt";
    private static final String GOOGLE_THICK_WHOIS_RESULT = "backend/src/test/resources/digital/google-thick-whois.txt";
    private static final String GOOGLE_THIN_WHOIS_RESULT = "backend/src/test/resources/digital/google-thin-whois.txt";
    private static final String YAHOO_WHOIS_RESULT = "backend/src/test/resources/digital/yahoo-whois.txt";
    private static final String YAHOO_THICK_WHOIS_RESULT = "backend/src/test/resources/digital/yahoo-thick-whois.txt";
    private static final String YAHOO_THIN_WHOIS_RESULT = "backend/src/test/resources/digital/yahoo-thin-whois.txt";
    private static final String VK_WHOIS_RESULT = "backend/src/test/resources/digital/vk-whois.txt";
    private static final String VK_THICK_WHOIS_RESULT = "backend/src/test/resources/digital/vk-thick-whois.txt";
    private static final String VK_THIN_WHOIS_RESULT = "backend/src/test/resources/digital/vk-thin-whois.txt";

    private static final String GOOGLE_NSLOOKUP_RESULT = "backend/src/test/resources/digital/google-nslookup.txt";
    private static final String YAHOO_NSLOOKUP_RESULT = "backend/src/test/resources/digital/yahoo-nslookup.txt";
    private static final String VK_NSLOOKUP_RESULT = "backend/src/test/resources/digital/vk-nslookup.txt";

    private static final String DOMAIN_NAME_PROPERTY = "domainName";
    private static final String MARK_MONITOR_SERVER = "whois.markmonitor.com ";
    private static final String NIC_RU_SERVER = "whois.nic.ru ";

    @Test
    void getGoogleWhoisJsonLinux() throws IOException, InterruptedException {
        BufferedReader googleWhoisBufferedReader = TestHelper.getBufferedReaderFromTxtFile(GOOGLE_WHOIS_RESULT);

        if (googleWhoisBufferedReader != null) {
            // test whois command output on linux os
            when(identityGenerator.whichOS()).thenReturn(OS.LINUX);
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_COMMAND + GOOGLE_DOMAIN))).thenReturn(googleWhoisBufferedReader);
            JSONObject googleWhoIsJson = digitalFootPrintService.getWhoIsJSON(GOOGLE_DOMAIN);

            assertAll(
                    () -> assertNotNull(googleWhoIsJson),
                    () -> assertTrue(StringUtils.equalsIgnoreCase(GOOGLE_DOMAIN, googleWhoIsJson.get(DOMAIN_NAME_PROPERTY).toString()))
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void getGoogleWhoisJsonMacOSX() throws IOException, InterruptedException {
        BufferedReader googleWhoisThinBufferedReader = TestHelper.getBufferedReaderFromTxtFile(GOOGLE_THIN_WHOIS_RESULT);
        BufferedReader googleWhoisThickBufferedReader = TestHelper.getBufferedReaderFromTxtFile(GOOGLE_THICK_WHOIS_RESULT);

        if (googleWhoisThickBufferedReader != null && googleWhoisThinBufferedReader != null) {
            // test whois command output from thick whois command on MAC OS X
            when(identityGenerator.whichOS()).thenReturn(OS.MAC_OS_X);
            // hard-coded thin whois command
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_THIN_COMMAND + GOOGLE_DOMAIN)))
                    .thenReturn(googleWhoisThinBufferedReader);
            // hard-coded thick whois command with whois server from the google-thin whois output in test resource
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_COMMAND + "-h " + MARK_MONITOR_SERVER + GOOGLE_DOMAIN)))
                    .thenReturn(googleWhoisThickBufferedReader);
            JSONObject googleWhoIsJson = digitalFootPrintService.getWhoIsJSON(GOOGLE_DOMAIN);

            assertAll(
                    () -> assertNotNull(googleWhoIsJson)
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void getYahooWhoisJsonLinux() throws IOException, InterruptedException {
        BufferedReader yahooWhoisBufferedReader = TestHelper.getBufferedReaderFromTxtFile(YAHOO_WHOIS_RESULT);

        if (yahooWhoisBufferedReader != null) {
            // test whois command output on linux os
            when(identityGenerator.whichOS()).thenReturn(OS.LINUX);
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_COMMAND + YAHOO_DOMAIN))).thenReturn(yahooWhoisBufferedReader);
            JSONObject yahooWhoIsJson = digitalFootPrintService.getWhoIsJSON(YAHOO_DOMAIN);

            assertAll(
                    () -> assertNotNull(yahooWhoIsJson),
                    () -> assertTrue(StringUtils.equalsIgnoreCase(YAHOO_DOMAIN, yahooWhoIsJson.get(DOMAIN_NAME_PROPERTY).toString()))
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void getYahooWhoisJsonMacOSX() throws IOException, InterruptedException {
        BufferedReader yahooWhoisThinBufferedReader = TestHelper.getBufferedReaderFromTxtFile(YAHOO_THIN_WHOIS_RESULT);
        BufferedReader yahooWhoisThickBufferedReader = TestHelper.getBufferedReaderFromTxtFile(YAHOO_THICK_WHOIS_RESULT);

        if (yahooWhoisThickBufferedReader != null && yahooWhoisThinBufferedReader != null) {
            // test whois command output from thick whois command on MAC OS X
            when(identityGenerator.whichOS()).thenReturn(OS.MAC_OS_X);
            // hard-coded thin whois command
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_THIN_COMMAND + YAHOO_DOMAIN)))
                    .thenReturn(yahooWhoisThinBufferedReader);
            // hard-coded thick whois command with whois server from the google-thin whois output in test resource
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_COMMAND + "-h " + MARK_MONITOR_SERVER + YAHOO_DOMAIN)))
                    .thenReturn(yahooWhoisThickBufferedReader);
            JSONObject yahooWhoIsJson = digitalFootPrintService.getWhoIsJSON(YAHOO_DOMAIN);

            assertAll(
                    () -> assertNotNull(yahooWhoIsJson)
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void getVKWhoisJsonLinux() throws IOException, InterruptedException {
        BufferedReader vkWhoisBufferedReader = TestHelper.getBufferedReaderFromTxtFile(VK_WHOIS_RESULT);

        if (vkWhoisBufferedReader != null) {
            // test whois command output on linux os
            when(identityGenerator.whichOS()).thenReturn(OS.LINUX);
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_COMMAND + VKONTAKTE_DOMAIN))).thenReturn(vkWhoisBufferedReader);
            JSONObject vkontakteWhoIsJson = digitalFootPrintService.getWhoIsJSON(VKONTAKTE_DOMAIN);

            assertAll(
                    () -> assertNotNull(vkontakteWhoIsJson),
                    () -> assertTrue(StringUtils.equalsIgnoreCase(VKONTAKTE_DOMAIN, vkontakteWhoIsJson.get(DOMAIN_NAME_PROPERTY).toString()))
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void getVKWhoisJsonMacOSX() throws IOException, InterruptedException {
        BufferedReader vkWhoisThinBufferedReader = TestHelper.getBufferedReaderFromTxtFile(VK_THIN_WHOIS_RESULT);
        BufferedReader vkWhoisThickBufferedReader = TestHelper.getBufferedReaderFromTxtFile(VK_THICK_WHOIS_RESULT);

        if (vkWhoisThickBufferedReader != null && vkWhoisThinBufferedReader != null) {
            // test whois command output from thick whois command on MAC OS X
            when(identityGenerator.whichOS()).thenReturn(OS.MAC_OS_X);
            // hard-coded thin whois command
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_THIN_COMMAND + VKONTAKTE_DOMAIN)))
                    .thenReturn(vkWhoisThinBufferedReader);
            // hard-coded thick whois command with whois server from the google-thin whois output in test resource
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_COMMAND + "-h " + NIC_RU_SERVER + VKONTAKTE_DOMAIN)))
                    .thenReturn(vkWhoisThickBufferedReader);
            JSONObject vkWhoIsJson = digitalFootPrintService.getWhoIsJSON(VKONTAKTE_DOMAIN);

            assertAll(
                    () -> assertNotNull(vkWhoIsJson)
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void getGoogleNSLookupJson() throws IOException, InterruptedException {
        BufferedReader googleNslookupBufferedReader = TestHelper.getBufferedReaderFromTxtFile(GOOGLE_NSLOOKUP_RESULT);

        if (googleNslookupBufferedReader != null) {
            when(fileService.processBufferedReader(getArgsCommandList(NSLOOKUP_COMMAND + GOOGLE_DOMAIN))).thenReturn(googleNslookupBufferedReader);
            JSONArray googleNslookupJsonArray = digitalFootPrintService.getNSLookupJSON(GOOGLE_DOMAIN);

            assertAll(
                    () -> assertNotNull(googleNslookupJsonArray)
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void getYahooNSLookupJson() throws IOException, InterruptedException {
        BufferedReader yahooNslookupBufferedReader = TestHelper.getBufferedReaderFromTxtFile(YAHOO_NSLOOKUP_RESULT);

        if (yahooNslookupBufferedReader != null) {
            when(fileService.processBufferedReader(getArgsCommandList(NSLOOKUP_COMMAND + YAHOO_DOMAIN))).thenReturn(yahooNslookupBufferedReader);
            JSONArray yahooNslookupJsonArray = digitalFootPrintService.getNSLookupJSON(YAHOO_DOMAIN);

            assertAll(
                    () -> assertNotNull(yahooNslookupJsonArray)
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void getVKNSLookupJson() throws IOException, InterruptedException {
        BufferedReader vkNSLookupBufferedReader = TestHelper.getBufferedReaderFromTxtFile(VK_NSLOOKUP_RESULT);

        if (vkNSLookupBufferedReader != null) {
            when(fileService.processBufferedReader(getArgsCommandList(NSLOOKUP_COMMAND + VKONTAKTE_DOMAIN))).thenReturn(vkNSLookupBufferedReader);
            JSONArray vkNSlookupJsonArray = digitalFootPrintService.getNSLookupJSON(VKONTAKTE_DOMAIN);

            assertAll(
                    () -> assertNotNull(vkNSlookupJsonArray)
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void getNSLookupFootprintList() throws IOException, InterruptedException {
        BufferedReader googleNslookupBufferedReader = TestHelper.getBufferedReaderFromTxtFile(GOOGLE_NSLOOKUP_RESULT);
        BufferedReader yahooNslookupBufferedReader = TestHelper.getBufferedReaderFromTxtFile(YAHOO_NSLOOKUP_RESULT);

        if (googleNslookupBufferedReader != null && yahooNslookupBufferedReader != null) {
            when(fileService.processBufferedReader(getArgsCommandList(NSLOOKUP_COMMAND + GOOGLE_DOMAIN))).thenReturn(googleNslookupBufferedReader);
            when(fileService.processBufferedReader(getArgsCommandList(NSLOOKUP_COMMAND + YAHOO_DOMAIN))).thenReturn(yahooNslookupBufferedReader);

            JSONArray yahooNsLookupJson = digitalFootPrintService.getNSLookupJSON(YAHOO_DOMAIN);
            JSONArray googleNsLookupJson = digitalFootPrintService.getNSLookupJSON(GOOGLE_DOMAIN);

            NSLookupFootPrintList yahooNsLookupList = digitalFootPrintService.getNSLookupFootprintList(yahooNsLookupJson);
            NSLookupFootPrintList googleNsLookupList = digitalFootPrintService.getNSLookupFootprintList(googleNsLookupJson);

            assertAll(
                    () -> assertNotNull(yahooNsLookupJson),
                    () -> assertNotNull(googleNsLookupJson),
                    () -> assertNotNull(yahooNsLookupList),
                    () -> assertNotNull(googleNsLookupList),
                    () -> assertEquals(YAHOO_DOMAIN, yahooNsLookupList.getNsLookupFootPrintList().get(0).getName()),
                    () -> assertEquals(GOOGLE_DOMAIN, googleNsLookupList.getNsLookupFootPrintList().get(0).getName())
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void getWhoIsFootprintLinux() throws IOException, InterruptedException {
        BufferedReader googleWhoisBufferedReader = TestHelper.getBufferedReaderFromTxtFile(GOOGLE_WHOIS_RESULT);
        BufferedReader yahooWhoisBufferedReader = TestHelper.getBufferedReaderFromTxtFile(YAHOO_WHOIS_RESULT);
        BufferedReader vkWhoisBufferedReader = TestHelper.getBufferedReaderFromTxtFile(VK_WHOIS_RESULT);

        if (googleWhoisBufferedReader != null && yahooWhoisBufferedReader != null && vkWhoisBufferedReader != null) {
            // test whois command output on linux os
            when(identityGenerator.whichOS()).thenReturn(OS.LINUX);

            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_COMMAND + GOOGLE_DOMAIN))).thenReturn(googleWhoisBufferedReader);
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_COMMAND + YAHOO_DOMAIN))).thenReturn(yahooWhoisBufferedReader);
            when(fileService.processBufferedReader(getArgsCommandList(WHO_IS_COMMAND + VKONTAKTE_DOMAIN))).thenReturn(vkWhoisBufferedReader);

            JSONObject googleObject = digitalFootPrintService.getWhoIsJSON(GOOGLE_DOMAIN);
            JSONObject yahooObject = digitalFootPrintService.getWhoIsJSON(YAHOO_DOMAIN);
            JSONObject vkontakteObject = digitalFootPrintService.getWhoIsJSON(VKONTAKTE_DOMAIN);

            WhoIsFootPrint googleFootprint = digitalFootPrintService.getWhoIsFootprint(googleObject);
            WhoIsFootPrint yahooFootprint = digitalFootPrintService.getWhoIsFootprint(yahooObject);
            WhoIsFootPrint vkontakteFootprint = digitalFootPrintService.getWhoIsFootprint(vkontakteObject);

            assertAll(
                    () -> assertNotNull(googleObject),
                    () -> assertNotNull(yahooObject),
                    () -> assertNotNull(vkontakteObject),
                    () -> assertTrue(StringUtils.equalsIgnoreCase(YAHOO_DOMAIN, yahooObject.getString(DOMAIN_NAME_PROPERTY))),
                    () -> assertTrue(StringUtils.equalsIgnoreCase(GOOGLE_DOMAIN, googleObject.getString(DOMAIN_NAME_PROPERTY))),
                    () -> assertTrue(StringUtils.equalsIgnoreCase(VKONTAKTE_DOMAIN, vkontakteObject.getString(DOMAIN_NAME_PROPERTY))),
                    () -> assertTrue(StringUtils.equalsIgnoreCase(YAHOO_DOMAIN, yahooFootprint.getDomainName())),
                    () -> assertTrue(StringUtils.equalsIgnoreCase(GOOGLE_DOMAIN, googleFootprint.getDomainName())),
                    () -> assertTrue(StringUtils.equalsIgnoreCase(VKONTAKTE_DOMAIN, vkontakteFootprint.getDomainName()))
            );
        } else {
            fail("Buffered Reader required for command process output is null");
        }
    }

    @Test
    void extractUrlsFromText() {
        List<String> urls = digitalFootPrintService.extractUrlsFromText(TEXT_WITH_URLS_1);
        assertEquals(5, urls.size());
    }

    @Test
    void isValidUrl() {
        boolean isValidUrlTrue_1 = digitalFootPrintService.isUrlValid(VALID_URL_1);
        boolean isValidUrlTrue_2 = digitalFootPrintService.isUrlValid(VALID_URL_2);
        assertTrue(isValidUrlTrue_1);
        assertTrue(isValidUrlTrue_2);
    }

    @Test
    void isValidUrlReturnsFalse() {
        boolean isValidUrlFalse_1 = digitalFootPrintService.isUrlValid(INVALID_URL_1);
        boolean isValidUrlFalse_2 = digitalFootPrintService.isUrlValid(INVALID_URL_2);
        assertFalse(isValidUrlFalse_1);
        assertFalse(isValidUrlFalse_2);
    }

    private List<String> getArgsCommandList(String command) {
        return List.of(command.split(" "));
    }
}