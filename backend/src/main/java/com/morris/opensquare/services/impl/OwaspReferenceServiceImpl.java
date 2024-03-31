package com.morris.opensquare.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morris.opensquare.models.notifications.OwaspBlogSnippet;
import com.morris.opensquare.services.OwaspReferenceService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class OwaspReferenceServiceImpl implements OwaspReferenceService {

    @Override
    public String getOwaspBlogSnippetsList() throws IOException, InterruptedException {
        InputStream owaspGithubInputStream = scrapOwaspGithubPage();
        String owaspBlogTree = buildOwaspBlogTreeBuilder(owaspGithubInputStream);
        List<OwaspBlogSnippet> owaspBlogSnippets = getOwaspBlogSnippets(owaspBlogTree);
        return owaspBlogSnippets.toString();
    }

    /**
     * Scraps the Owasp GitHub page to create an InputStream for processing.
     *
     * @return ByteArrayInputStream
     * @see ByteArrayInputStream
     */
    private InputStream scrapOwaspGithubPage() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://github.com/OWASP/owasp.github.io/tree/main/_posts"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new ByteArrayInputStream(response.body().getBytes());
    }

    /**
     * Processes the markup from Owasp GitHub page and reads for a line that contains
     * 'refType'. This line contains the list of Owasp blog posts. This line comes in
     * the form of a JSON object string, this helps to substring the line that contains only
     * correct json for further processing.
     *
     * @param owaspBlogResponseStream InputStream
     * @return String
     */
    private String buildOwaspBlogTreeBuilder(InputStream owaspBlogResponseStream) {
        StringBuilder treeBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(owaspBlogResponseStream));

        bufferedReader.lines().forEach(line -> {
            if (line.contains("refType")) {
                treeBuilder.append(line);
            }
        });
        return treeBuilder.substring(71);
    }

    /**
     * From the Owasp blog posts, we have a tree, that can be marshalled to a JSONObject
     * and queried for various keys. The key this function needs is the tree key, which
     * returns an object that contains a list of the Owasp blog posts. This list is
     * marshalled into a List of OwaspBlogSnippets.
     *
     * @return String
     * @see OwaspBlogSnippet
     */
    private List<OwaspBlogSnippet> getOwaspBlogSnippets(String owaspTree) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject(owaspTree.substring(0, owaspTree.length() - 9));
        JSONObject payload = jsonObject.getJSONObject("payload").getJSONObject("tree");
        JSONArray blogs = payload.getJSONArray("items");
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<OwaspBlogSnippet>> arrayType = new TypeReference<> (){};
        return objectMapper.readValue(blogs.toString(), arrayType);
    }
}
