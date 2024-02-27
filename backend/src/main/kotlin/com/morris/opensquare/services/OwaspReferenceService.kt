package com.morris.opensquare.services

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.morris.opensquare.models.Notifications.OwaspBlogSnippet
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * Various methods that expose actions to marshall OWASP content queried from sources
 * such as the OWASP Organization's official GitHub page. These sources are used as
 * "references" to many of the features in Opensquare.
 */
@Service
class OwaspReferenceService {

    /**
     * Scraps the Owasp GitHub page to pull all blogs. These blogs are than marshalled
     * into a list of OwaspBlogSnippets.
     *
     * @return String
     * @see OwaspBlogSnippet
     */
    fun getOwaspBlogSnippets(): String {
        val owaspGithubInputStream = scrapOwaspGithubPage()
        val owaspBlogTree = buildOwaspBlogTreeBuilder(owaspGithubInputStream)
        val owaspBlogSnippets = getOwaspBlogSnippets(owaspBlogTree)
        return owaspBlogSnippets.toString()
    }

    /**
     * Scraps the Owasp GitHub page to create an InputStream for processing.
     *
     * @return ByteArrayInputStream
     * @see ByteArrayInputStream
     */
    private fun scrapOwaspGithubPage(): InputStream {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://github.com/OWASP/owasp.github.io/tree/main/_posts"))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return ByteArrayInputStream(response.body().toByteArray())
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
    private fun buildOwaspBlogTreeBuilder(owaspBlogResponseStream: InputStream): String {
        val treeBuilder: StringBuilder = StringBuilder()
        val responseBufferedRead = owaspBlogResponseStream.bufferedReader().use(BufferedReader::readText)

        responseBufferedRead.reader().forEachLine { line ->
            if (line.contains("refType")) {
                treeBuilder.append(line)
            }
        }
        return treeBuilder.toString().substring(71)
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
    private fun getOwaspBlogSnippets(owaspTree: String): List<OwaspBlogSnippet> {
        val jsonObject = JSONObject(owaspTree.substring(0, owaspTree.length - 9))
        val payload = jsonObject.getJSONObject("payload").getJSONObject("tree")
        val blogs = payload.getJSONArray("items")
        val objectMapper = ObjectMapper()
        val arrayType: TypeReference<List<OwaspBlogSnippet>> = object: TypeReference<List<OwaspBlogSnippet>>(){}
        return objectMapper.readValue(blogs.toString(), arrayType)
    }
}