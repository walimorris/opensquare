package com.morris.opensquare.controllers;

import com.morris.opensquare.services.OwaspReferenceService
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/opensquare")
class OwaspReferenceController @Autowired constructor(private val owaspReferenceService: OwaspReferenceService) {

    @CrossOrigin(origins = ["https://cloud.mongodb.com"])
    @GetMapping("/owasp/posts")
    fun getLatestOwaspBlog(httpServletRequest: HttpServletRequest): ResponseEntity<out Any> {
        println("API triggered")
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(owaspReferenceService.getOwaspBlogSnippets())
    }
}
