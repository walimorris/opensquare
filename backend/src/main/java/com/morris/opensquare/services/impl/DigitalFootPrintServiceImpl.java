package com.morris.opensquare.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.morris.opensquare.models.digitalfootprints.*;
import com.morris.opensquare.services.DigitalFootPrintService;
import com.morris.opensquare.services.FileService;
import com.morris.opensquare.services.IdentityGenerator;
import com.morris.opensquare.services.loggers.LoggerService;
import com.morris.opensquare.utils.Constants;
import com.morris.opensquare.utils.ExternalServiceUtil;
import com.twitter.twittertext.Extractor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.morris.opensquare.utils.Constants.*;

@Service
public class DigitalFootPrintServiceImpl implements DigitalFootPrintService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DigitalFootPrintServiceImpl.class);

    private static final String LAST_UPDATE_OF_WHOIS_DATABASE = "lastUpdateOfWhoIsDatabase";
    private static final String BACKLINK_SITE_WILDCARD = "site:* ";
    private static final String BACKLINK_SITE_APPEND = " -site:";
    private static final String STATE_PROVINCE_WITH_FORWARD_SLASH = "State/Province";
    private static final String STATE_PROVINCE = "StateProvince";
    private static final String STREET = "Street";
    private static final String URL_OF_ICANN = "URL of the ICANN";
    private static final String URL_ICANN_COMPLAINT_FORM = "urlIcannComplaintForm";
    private static final String LAST_UPDATE = "Last update";
    private static final String REGISTRAR_WHO_IS_SERVER = "Registrar WHOIS Server";

    private final ExternalServiceUtil externalServiceUtil;
    private final LoggerService loggerService;
    private final FileService fileService;
    private final IdentityGenerator identityGenerator;

    @Autowired
    public DigitalFootPrintServiceImpl(ExternalServiceUtil externalServiceUtil, LoggerService loggerService,
                                       FileService fileService, IdentityGenerator identityGenerator) {

        this.externalServiceUtil = externalServiceUtil;
        this.loggerService = loggerService;
        this.fileService = fileService;
        this.identityGenerator = identityGenerator;
    }

    @Override
    public JSONArray getNSLookupJSON(@NonNull String domain) throws IOException, InterruptedException {
        JSONArray nsLookupJSONArray = new JSONArray();
        String s;
        BufferedReader bufferedReader = fileService.processBufferedReader(NSLOOKUP_COMMAND + domain);
        Thread.sleep(2000);
        int line = 0;
        JSONObject nsLookupJSONObject = new JSONObject();
        while ((s = bufferedReader.readLine()) != null) {
            line++;
            if (line >= 5 && s.contains(REGEX_COLON)) {
                String[] parts = s.split(REGEX_COLON);
                if (parts.length == 2) {
                    nsLookupJSONObject.put(parts[0].toLowerCase(), parts[1].trim());
                } else {
                    nsLookupJSONObject.put(parts[0].toLowerCase(), concatIPv6(parts));
                }
                if (nsLookupJSONObject.length() == 2) {
                    nsLookupJSONArray.put(nsLookupJSONObject);
                    nsLookupJSONObject = new JSONObject();
                }
            }
        }
        bufferedReader.close();
        return nsLookupJSONArray;
    }

    @Override
    public JSONObject getWhoIsJSON(@NonNull String domain) throws IOException, InterruptedException {
        OS os = identityGenerator.whichOS();
        String command = getWhoisCommandBasedOnOperatingSystem(domain, os);
        JSONObject whoisJson = new JSONObject();
        if (command != null) {
            String s;
            BufferedReader bufferedReader = fileService.processBufferedReader(command);
            Thread.sleep(2000);

            if (os.equals(OS.MAC_OS_X)) {
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains(REGEX_COLON)) {
                        // this is the last line in whois response that's parsed.
                        // if present in whois json, this process should break.
                        if (whoisJson.opt(LAST_UPDATE_OF_WHOIS_DATABASE) != null) {
                            break;
                        }
                        if (s.contains(REGEX_COLON)) {
                            parseWhoIsParts(s, whoisJson);
                        }
                    }
                }
            } else {
                boolean marked = false;
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains(REGISTRARS)) {
                        marked = true;
                    }
                    if (marked && s.contains(REGEX_COLON)) {
                        // this is the last line in whois response that's parsed.
                        // if present in whois json, this process should break.
                        if (whoisJson.opt(LAST_UPDATE_OF_WHOIS_DATABASE) != null) {
                            break;
                        }
                        if (s.contains(REGEX_COLON)) {
                            parseWhoIsParts(s, whoisJson);
                        }
                    }
                }
            }
            bufferedReader.close();
        }
        return whoisJson;
    }

    @Override
    public WhoIsFootPrint getWhoIsFootprint(@NonNull JSONObject whoIsJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(whoIsJson.toString(), WhoIsFootPrint.class);
    }

    public NSLookupFootPrintList getNSLookupFootprintList(@NonNull JSONArray nsLookupJsonArray) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<NSLookupFootPrint>> reference = new TypeReference<>() {};
        List<NSLookupFootPrint> nsLookupFootPrints = mapper.readValue(nsLookupJsonArray.toString(), reference);
        return new NSLookupFootPrintList(nsLookupFootPrints);
    }

    @Override
    public List<String> extractUrlsFromText(@NonNull String text) {
        return new Extractor().extractURLs(text);
    }

    @Override
    public boolean isUrlValid(@NonNull String url) {
        try {
            new URI(url).toURL().toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            loggerService.saveLog(e.getClass().getName(), ERROR_VALIDATING_URL + REGEX_EMPTY + url + REGEX_EMPTY + e.getMessage(), Optional.of(LOGGER));
        }
        return false;
    }

    @Override
    public List<GoogleResultWrapper> getBacklinksFromUrl(@NonNull String applicationName,
                                            @NonNull String googleApiKey,
                                            @NonNull String engineCx,
                                            @NonNull String url) throws IOException {

        Customsearch customGoogleSearch = externalServiceUtil.getGoogleCustomSearchService(
                applicationName, googleApiKey);
        StringBuilder query = new StringBuilder()
                .append(BACKLINK_SITE_WILDCARD)
                .append(url)
                .append(BACKLINK_SITE_APPEND)
                .append(url);

        Customsearch.Cse.List list = customGoogleSearch.cse()
                .list(query.toString())
                .setCx(engineCx);

        Search searchResult = list.execute();
        return getGoogleResultsWrapperList(searchResult.getItems());
    }

    private List<GoogleResultWrapper> getGoogleResultsWrapperList(List<Result> googleSearchResults) {
        List<GoogleResultWrapper> results = new ArrayList<>();
        for (Result result : googleSearchResults) {
            results.add(new GoogleResultWrapper.Builder()
                    .pageMap(result.getPagemap())
                    .snippet(result.getSnippet())
                    .title(result.getTitle())
                    .kind(result.getKind())
                    .cacheId(result.getCacheId())
                    .displayLink(result.getDisplayLink())
                    .formattedUrl(result.getFormattedUrl())
                    .htmlSnippet(result.getHtmlSnippet())
                    .htmlTitle(result.getHtmlTitle())
                    .build());
        }
        return results;
    }

    private void parseWhoIsParts(@NonNull String s, @NonNull JSONObject whoisJson) {
        String[] parts = s.split(Constants.REGEX_COLON_WITH_SPACE);
        int partsSize = parts.length;
        if (partsSize < 2) {
            // need to remove colon when property has no value - since colon is what whois response is split on
            // when property has no response, the property will split with the colon as part of part[0]
            whoisJson.put(parseWhoIsPropertyToJsonProperty(parts[0].substring(0, parts[0].length() - 1).trim()), Constants.REDACTED);
        } else if (parts[0].contains(STATE_PROVINCE_WITH_FORWARD_SLASH)) {
            String[] subParts = parts[0].split(REGEX_EMPTY);
            whoisJson.put(subParts[0].toLowerCase() + STATE_PROVINCE, parts[1].trim());
        } else if (parts[0].contains(STREET)) {
            String[] subParts = parts[0].split(REGEX_EMPTY);
            String property = subParts[0].toLowerCase() + subParts[1];
            if (!containsStreetProperty(whoisJson, property)) {
                whoisJson.put(parseWhoIsPropertyToJsonProperty(parts[0].trim()), parts[1].trim());
            }
        } else if (parts[0].contains(URL_OF_ICANN)) {
            whoisJson.put(URL_ICANN_COMPLAINT_FORM, parts[1].trim());
        } else if (parts[0].contains(LAST_UPDATE)) {
            // the whois report contains three trailing >>> characters which need to be removed
            whoisJson.put(LAST_UPDATE_OF_WHOIS_DATABASE, parts[1].substring(0, parts[1].length() - 3).trim());
        } else {
            whoisJson.put(parseWhoIsPropertyToJsonProperty(parts[0].trim()), parts[1].trim());
        }
    }

    private boolean containsStreetProperty(@NonNull JSONObject whoisJson, @NonNull String property) {
        return whoisJson.opt(property) != null;
    }

    private String parseWhoIsPropertyToJsonProperty(@NonNull String property) {
        if (property.contains(REGEX_EMPTY)) {
            String[] parts = property.split(REGEX_EMPTY);
            StringBuilder fullText = new StringBuilder();
            fullText.append(parts[0].toLowerCase());
            for (int i = 1; i < parts.length; i++) {
                fullText.append(
                                parts[i].substring(0, 1).toUpperCase())
                        .append(parts[i]
                                .substring(1)
                                .toLowerCase());
            }
            return fullText.toString();
        }
        return property.toLowerCase();
    }

    private String concatIPv6(@NonNull String[] parts) {
        StringBuilder ipv6Builder = new StringBuilder(REGEX_COLON);
        for (int i = 1; i < parts.length; i++) {
            if (i < parts.length - 1) {
                ipv6Builder.append(parts[i]).append(REGEX_COLON);
            } else ipv6Builder.append(parts[i]);
        }
        return ipv6Builder.toString();
    }

    /**
     * Different operating system will return different output for a whois command. Most times, "thin whois' output
     * will not contain enough information about a domain. Based on operating system, a client may need to express
     * a more involved whois command.
     *
     * @param domain whois domain to search
     * @param os operating system
     *
     * @return {@link String} whois command based on operating system
     *
     * @throws InterruptedException
     * @throws IOException
     */
    private String getWhoisCommandBasedOnOperatingSystem(String domain, OS os) throws InterruptedException, IOException {
        // get os and parse whois output
        if (os.equals(OS.LINUX)) {
            return WHO_IS_COMMAND + domain;
        }
        String thinWhoisQuery = queryThinWhoisServerForRegistrarWhoisServer(domain);
        if (thinWhoisQuery != null) {
            StringBuilder command = new StringBuilder()
                    .append(WHO_IS_COMMAND)
                    .append("-h ")
                    .append(thinWhoisQuery)
                    .append(REGEX_EMPTY)
                    .append(domain);

            return command.toString();
        }
        return null;
    }

    /**
     * The "Thin whois" is the very basic output from whois command. It won't contain much of a
     * registered domain's resource information. Mac OS X will return a thin whois response.
     * With this "thin whois" comes the Registrar whois server, which allows a client to query
     * the registrar whois server for a more complete whois output. Use this method to return the
     * registrar whois server of any domain.
     *
     * @param domain {@link String} domain to search
     *
     * @return {@link String} registrar whois server of domain
     *
     * @throws InterruptedException
     * @throws IOException
     */
    private String queryThinWhoisServerForRegistrarWhoisServer(String domain) throws InterruptedException, IOException {
        String command = WHO_IS_THIN_COMMAND + domain;
        BufferedReader bufferedReader = fileService.processBufferedReader(command);
        Thread.sleep(2000);
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            if (s.contains(REGISTRAR_WHO_IS_SERVER)) {
                bufferedReader.close();
                return s.split(REGEX_COLON)[1].trim();
            }
        }
        bufferedReader.close();
        return null;
    }
}
