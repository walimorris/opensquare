package com.morris.opensquare.controllers;

import com.morris.opensquare.services.RagChainService;
import com.morris.opensquare.services.impl.YouTubeRagChainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/opensquare/api/rag/youtube")
public class YouTubeRagChainController {
    private final RagChainService ragChainService;

    @Autowired
    public YouTubeRagChainController(YouTubeRagChainServiceImpl youTubeRagChainService) {
        this.ragChainService = youTubeRagChainService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> getPromptResponse(@RequestParam String prompt) {
        String promptResponse = ragChainService.promptResponse(prompt);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(promptResponse);
    }
}
