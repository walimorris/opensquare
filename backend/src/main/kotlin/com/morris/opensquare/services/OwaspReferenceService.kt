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

@Service
class OwaspReferenceService {

    fun getOwaspGithubPage(): String {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://github.com/OWASP/owasp.github.io/tree/main/_posts"))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val treeBuilder: StringBuilder = StringBuilder()
        val responseStream: InputStream = ByteArrayInputStream(response.body().toByteArray())
        val responseBufferedRead = responseStream.bufferedReader().use(BufferedReader::readText)

        responseBufferedRead.reader().forEachLine { line ->
            if (line.contains("refType")) {
                treeBuilder.append(line)
            }
        }
        val jsonStr = treeBuilder.toString().substring(71)
        val jsonObject = JSONObject(jsonStr.substring(0, jsonStr.length - 9))
        val payload = jsonObject.getJSONObject("payload").getJSONObject("tree")
        val blogs = payload.getJSONArray("items")

        val objectMapper = ObjectMapper()
        val arrayType: TypeReference<List<OwaspBlogSnippet>> = object: TypeReference<List<OwaspBlogSnippet>>(){}
        val blogSnippets: List<OwaspBlogSnippet> = objectMapper.readValue(blogs.toString(), arrayType)
        return blogs.toString()
    }
}