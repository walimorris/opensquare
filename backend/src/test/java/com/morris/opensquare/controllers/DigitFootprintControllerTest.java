package com.morris.opensquare.controllers;

import com.google.api.services.customsearch.model.Result;
import com.morris.opensquare.TestHelper;
import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import com.morris.opensquare.models.digitalfootprints.*;
import com.morris.opensquare.services.DigitalFootPrintService;
import com.morris.opensquare.utils.ApplicationConfigurationUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DigitFootprintControllerTest {

    @MockBean
    private DigitalFootPrintService digitalFootPrintService;

    @MockBean
    private ApplicationConfigurationUtil applicationConfigurationUtil;

    @Autowired
    MockMvc mockModelViewController;

    private static WhoIsFootPrint whoIsFootPrint;
    private static NSLookupFootPrintList nsLookupFootPrintList;
    private static List<GoogleResultWrapper> backLinksResult;
    private static JSONObject whoisJSON;
    private static String nslookupString;
    private static String whoisString;
    private static String backLinksString;
    private static UrlRequest urlRequest;

    private static final String WHO_IS_FOOTPRINT_JSON = "backend/src/test/resources/models/WhoIsFootPrint.json";
    private static final String NSLOOKUP_JSON = "backend/src/test/resources/models/NSLookupFootPrintList.json";
    private static final String BACK_LINKS_RESULT_JSON = "backend/src/test/resources/google-custom-search.json";
    private static final String WHOIS_REQUEST = "/opensquare/api/footprints/whois";
    private static final String NS_LOOKUP_REQUEST = "/opensquare/api/footprints/nslookup";
    private static final String BACK_LINKS_REQUEST = "/opensquare/api/footprints/backlinks";

    private static final String DOMAIN = "domain";
    private static final String GOOGLE = "google.com";
    private static final String YAHOO = "https://yahoo.com";
    private static final String BAD_URL = "dd.yahoo.jam";

    private static final String APPLICATION_NAME = "test-application";
    private static final String GOOGLE_KEY = "fake-key-xyz";
    private static final String ENGINE_CX = "123a456b789c";

    @BeforeEach
    void setUp() throws IOException {
        whoIsFootPrint = getWhoIsFootPrint();
        whoisString = TestHelper.writeValueAsString(whoIsFootPrint);
        whoisJSON = TestHelper.getJSONFromObject(whoisString);

        nsLookupFootPrintList = getNsLookupFootPrintList();
        nslookupString = TestHelper.writeValueAsString(nsLookupFootPrintList);

        backLinksResult = getBackLinksResult();
        backLinksString = TestHelper.writeValueAsString(backLinksResult);
        urlRequest = getUrlRequest();
    }

    @Test
    void getWhoisFootprint() throws Exception {
        when(digitalFootPrintService.getWhoIsJSON(GOOGLE)).thenReturn(whoisJSON);
        when(digitalFootPrintService.getWhoIsFootprint(whoisJSON)).thenReturn(whoIsFootPrint);
        this.mockModelViewController.perform(get(WHOIS_REQUEST).param(DOMAIN, GOOGLE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(whoisString));
    }

    @Test
    void getNslookupList() throws Exception {
        when(digitalFootPrintService.getNSLookupJSON(GOOGLE)).thenReturn(new JSONArray());
        when(digitalFootPrintService.getNSLookupFootprintList(new JSONArray())).thenReturn(nsLookupFootPrintList);
        this.mockModelViewController.perform(get(NS_LOOKUP_REQUEST).param(DOMAIN, GOOGLE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(nslookupString));
    }

    @Test
    void getBacklinks() throws Exception {
        ApplicationPropertiesConfiguration applicationPropertiesConfiguration = Mockito.mock(ApplicationPropertiesConfiguration.class);
        when(digitalFootPrintService.isUrlValid(YAHOO)).thenReturn(true);

        when(applicationConfigurationUtil.getApplicationPropertiesConfiguration()).thenReturn(applicationPropertiesConfiguration);

        when(applicationConfigurationUtil.getApplicationPropertiesConfiguration().googleAppName()).thenReturn(APPLICATION_NAME);
        when(applicationConfigurationUtil.getApplicationPropertiesConfiguration().googleApiKey()).thenReturn(GOOGLE_KEY);
        when(applicationConfigurationUtil.getApplicationPropertiesConfiguration().engineCx()).thenReturn(ENGINE_CX);
        when(digitalFootPrintService.getBacklinksFromUrl(APPLICATION_NAME, GOOGLE_KEY, ENGINE_CX, urlRequest.getUrl())).thenReturn(backLinksResult);

        this.mockModelViewController.perform(get(BACK_LINKS_REQUEST).param("url", YAHOO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(backLinksString));
    }

    @Test
    void getBacklinksWithMalformedUrl() throws Exception {
        when(digitalFootPrintService.isUrlValid(BAD_URL)).thenReturn(false);

        this.mockModelViewController.perform(get(BACK_LINKS_REQUEST).param("url", BAD_URL))
                .andExpect(status().isBadRequest());
    }

    private WhoIsFootPrint getWhoIsFootPrint() throws IOException {
        return TestHelper.convertModelFromFile(WHO_IS_FOOTPRINT_JSON, WhoIsFootPrint.class);
    }

    @SuppressWarnings("unchecked")
    private NSLookupFootPrintList getNsLookupFootPrintList() throws IOException {
        List<NSLookupFootPrint> nsLookupFootPrints = (List<NSLookupFootPrint>) TestHelper.convertModelFromFile(NSLOOKUP_JSON, List.class, NSLookupFootPrint.class);
        return new NSLookupFootPrintList(nsLookupFootPrints);
    }

    @SuppressWarnings("unchecked")
    private List<GoogleResultWrapper> getBackLinksResult() throws IOException {
        return (List<GoogleResultWrapper>) TestHelper.convertModelFromFile(BACK_LINKS_RESULT_JSON, List.class, Result.class);
    }

    private UrlRequest getUrlRequest() {
        return new UrlRequest(YAHOO);
    }
}
