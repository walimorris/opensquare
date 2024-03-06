package com.morris.opensquare.controllers;

import com.google.api.services.youtube.model.CommentSnippet;
import com.morris.opensquare.models.youtube.YouTubeTranscribeSegment;
import com.morris.opensquare.services.YouTubeService;
import com.morris.opensquare.utils.ApplicationConfigurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/opensquare/api/youtube")
public class YouTubeController {

    private final YouTubeService youTubeService;
    private final ApplicationConfigurationUtil applicationConfigurationUtil;
    private final String key;
    private final String app;

    @Autowired
    public YouTubeController(YouTubeService youTubeService, ApplicationConfigurationUtil applicationConfigurationUtil) {

        this.youTubeService = youTubeService;
        this.applicationConfigurationUtil = applicationConfigurationUtil;
        this.key = this.applicationConfigurationUtil.getApplicationPropertiesConfiguration().googleApiKey();
        this.app = this.applicationConfigurationUtil.getApplicationPropertiesConfiguration().appName();
    }

    @GetMapping("/topLevelComments")
    public ResponseEntity<List<CommentSnippet>> getAllTopLevelCommentsFromVideo(@RequestParam String videoId) {
        List<CommentSnippet> topLevelComments = youTubeService.getTopLevelCommentsFromYouTubeVideo(app, key, videoId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(topLevelComments);
    }

    @GetMapping("/en/transcribe")
    public ResponseEntity<List<YouTubeTranscribeSegment>> getYouTubeVideoTranscriptSegments(@RequestParam String videoId) {
        List<YouTubeTranscribeSegment> segments = youTubeService.getYouTubeTranscribeSegmentsFromVideoId(videoId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(segments);
    }
}
