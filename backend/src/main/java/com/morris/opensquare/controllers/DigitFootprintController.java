package com.morris.opensquare.controllers;

import com.google.api.services.customsearch.model.Result;
import com.morris.opensquare.models.digitalfootprints.DomainRequest;
import com.morris.opensquare.models.digitalfootprints.NSLookupFootPrintList;
import com.morris.opensquare.models.digitalfootprints.UrlRequest;
import com.morris.opensquare.models.digitalfootprints.WhoIsFootPrint;
import com.morris.opensquare.services.DigitalFootPrintService;
import com.morris.opensquare.utils.ApplicationConfigurationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/opensquare/api/footprints")
public class DigitFootprintController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DigitFootprintController.class);

    private final DigitalFootPrintService digitalFootPrintService;
    private final ApplicationConfigurationUtil applicationConfigurationUtil;

    @Autowired
    public DigitFootprintController(DigitalFootPrintService digitalFootPrintService,
                                    ApplicationConfigurationUtil applicationConfigurationUtil) {
        this.digitalFootPrintService = digitalFootPrintService;
        this.applicationConfigurationUtil = applicationConfigurationUtil;
    }

    @GetMapping("/whois")
    public ResponseEntity<WhoIsFootPrint> getWhoisFootprint(@Validated DomainRequest whoIsRequest, HttpServletRequest request) throws IOException, InterruptedException {
        LOGGER.info("SEARCH DOMAIN: {}", whoIsRequest.getDomain());
        HttpSession session = request.getSession();
        String requestedDomain = whoIsRequest.getDomain();

        // get the whois object
        JSONObject whoisJSON = digitalFootPrintService.getWhoIsJSON(requestedDomain);
        WhoIsFootPrint whoIsFootPrint = digitalFootPrintService.getWhoIsFootprint(whoisJSON);
        session.setAttribute("whois", whoIsFootPrint);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(whoIsFootPrint);
    }

    @GetMapping("/nslookup")
    public ResponseEntity<NSLookupFootPrintList> getNslookupList(@Validated DomainRequest nslookupRequest, HttpServletRequest request) throws IOException, InterruptedException {
        LOGGER.info("SEARCH DOMAIN: {}", nslookupRequest.getDomain());
        HttpSession session = request.getSession();
        String requestedDomain = nslookupRequest.getDomain();

        // get the nslookup object
        JSONArray nslookupJSON = digitalFootPrintService.getNSLookupJSON(requestedDomain);
        NSLookupFootPrintList nsLookupFootPrintList = digitalFootPrintService.getNSLookupFootprintList(nslookupJSON);
        session.setAttribute("nslookup", nsLookupFootPrintList);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(nsLookupFootPrintList);
    }

    @GetMapping("/backlinks")
    public ResponseEntity<List<Result>> getBacklinks(@Validated UrlRequest urlRequest, HttpServletRequest httpServletRequest) throws IOException {
        LOGGER.info("URL SEARCH: {}", urlRequest.getUrl());
        HttpSession httpSession = httpServletRequest.getSession();

        if (digitalFootPrintService.isUrlValid(urlRequest.getUrl())) {
            String googleSearchAppName = applicationConfigurationUtil.getApplicationPropertiesConfiguration().googleAppName();
            String googleKey = applicationConfigurationUtil.getApplicationPropertiesConfiguration().googleApiKey();
            String engineCx = applicationConfigurationUtil.getApplicationPropertiesConfiguration().engineCx();

            List<Result> results = digitalFootPrintService.getBacklinksFromUrl(googleSearchAppName, googleKey, engineCx, urlRequest.getUrl());
            httpSession.setAttribute("backlinks", results);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(results);
        }
        return ResponseEntity.badRequest()
                .body(null);
    }
}
