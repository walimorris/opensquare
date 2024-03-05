package com.morris.opensquare.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.services.customsearch.model.Result;
import com.morris.opensquare.models.digitalfootprints.GoogleResultWrapper;
import com.morris.opensquare.models.digitalfootprints.NSLookupFootPrint;
import com.morris.opensquare.models.digitalfootprints.NSLookupFootPrintList;
import com.morris.opensquare.models.digitalfootprints.WhoIsFootPrint;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;

public interface DigitalFootPrintService {

    /**
     * Creates and gets the result of a whois command in json format.<br><br>
     *
     * WHOIS is used for many purposes. Under ICANN organization's agreements,
     * WHOIS may be used for any lawful purposes except to enable marketing or
     * spam, or to enable high volume, automated processes to query a registrar
     * or registry's systems, except to manage domain names. In addition to
     * identifying domain name registrants, WHOIS data also allows network
     * administrators and others to find and fix system problems and to maintain
     * Internet stability. With it, they can determine the availability of domain
     * names, combat spam or fraud, identify trademark infringement and enhance
     * accountability of domain name registrants. WHOIS data is sometimes used to
     * track down and identify domain name registrants who may be posting illegal
     * content or engaging in phishing scams. These are just a few examples of how
     * WHOIS helps maintain a healthy Internet ecosystem.<br><br>
     *
     * @param domain {@link WhoIsFootPrint} domain name (example: google.com)
     * @see <a href="https://whois.icann.org/en/basics-whois">ICANN-Whois</a>
     *
     * @return {@link JSONObject}
     * @throws IOException
     */
    JSONObject getWhoIsJSON(@NonNull String domain) throws IOException, InterruptedException;

    /**
     * Creates and gets the result of a NSLookup command in json format.<br><br>
     *
     * Displays information that you can use to diagnose Domain Name System (DNS)
     * infrastructure. Before using this tool, you should be familiar with how DNS
     * works.<br><br>
     *
     * @param domain {@link NSLookupFootPrint} domain name (example: google.com)
     * @see <a href="https://learn.microsoft.com/en-us/windows-server/administration/windows-commands/nslookup">NSLookup Docs</a>
     *
     * @return {@link JSONArray}
     *
     * @throws IOException
     * @throws InterruptedException
     */
    JSONArray getNSLookupJSON(@NonNull String domain) throws IOException, InterruptedException;

    /**
     * Gets {@link NSLookupFootPrintList} object from {@link JSONArray} of objects that contain
     * {@link NSLookupFootPrint} properties marshalled from json roperties.
     *
     * @param nsLookupJsonArray {@link JSONArray}
     *
     * @return {@link NSLookupFootPrintList}
     * @throws JsonProcessingException
     */
    NSLookupFootPrintList getNSLookupFootprintList(@NonNull JSONArray nsLookupJsonArray) throws JsonProcessingException;

    /**
     * Marshall a Whois {@link JSONObject} to a {@link WhoIsFootPrint}
     *
     * @param whoIsJson {@link JSONObject}
     *
     * @return {@link WhoIsFootPrint}
     * @throws JsonProcessingException
     */
    WhoIsFootPrint getWhoIsFootprint(@NonNull JSONObject whoIsJson) throws JsonProcessingException;

    /**
     * Extracts any urls from given text.
     *
     * @param text {@link String}
     *
     * @return {@link List<String>}
     */
    List<String> extractUrlsFromText(@NonNull String text);

    /**
     * Checks validity of url.
     *
     * @param url {@link String}
     *
     * @return boolean
     */
    boolean isUrlValid(@NonNull String url);

    /**
     * Returns a list of top 10 backlinks of a given url.<br><br>
     *
     * What's a backlink? A backlink is a link on a page from another
     * site that links to a page on your site. It links back to your site.
     * Further, in security, you can check a sites backlinks to help verify
     * the safety or security of a site, to say the least.
     *
     * @param applicationName {@link String} google application name
     * @param googleApiKey {@link String} google api key
     * @param engineCx {@link String} application's search engine id
     * @param url {@link String}
     *
     * @see <a href="https://support.google.com/webmasters/answer/9049606?hl=en">What is a backlink?</a>
     * @see <a href="https://developers.google.com/custom-search/v1/reference/rest/v1/cse/list?apix_params=%7B%22cx%22%3A%22309081fb7841e4edc%22%2C%22q%22%3A%22https%3A%2F%2Fyahoo.com%22%7D">Google Custom Search API Doc</a>
     *
     * @return {@link List<Result>}
     * @throws IOException
     */
    List<GoogleResultWrapper> getBacklinksFromUrl(@NonNull String applicationName,
                                                  @NonNull String googleApiKey,
                                                  @NonNull String engineCx,
                                                  @NonNull String url) throws IOException;
}
