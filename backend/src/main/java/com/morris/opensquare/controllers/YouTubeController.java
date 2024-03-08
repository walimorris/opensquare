package com.morris.opensquare.controllers;

import com.google.api.services.youtube.model.CommentSnippet;
import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import com.morris.opensquare.models.youtube.YouTubeTranscribeSegment;
import com.morris.opensquare.models.youtube.YouTubeVideo;
import com.morris.opensquare.services.YouTubeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeController.class);

    private static final String SNIPPET = "snippet";
    private static final String SNIPPET_REPLIES = "snippet,replies";
    private final YouTubeService youTubeService;
    private final ApplicationPropertiesConfiguration applicationPropertiesConfiguration;
    private final String googleKey;
    private final String openaiKey;
    private final String app;

    @Autowired
    public YouTubeController(YouTubeService youTubeService, ApplicationPropertiesConfiguration applicationPropertiesConfiguration) {

        this.youTubeService = youTubeService;
        this.applicationPropertiesConfiguration = applicationPropertiesConfiguration;
        this.googleKey = this.applicationPropertiesConfiguration.googleApiKey();
        this.openaiKey = this.applicationPropertiesConfiguration.openAI();
        this.app = this.applicationPropertiesConfiguration.appName();
    }

    @GetMapping("/topLevelComments")
    public ResponseEntity<List<CommentSnippet>> getAllTopLevelCommentsFromVideo(@RequestParam String videoId) {
        List<CommentSnippet> topLevelComments = youTubeService.getTopLevelCommentsFromYouTubeVideo(app, googleKey, videoId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(topLevelComments);
    }

    @GetMapping("/en/video")
    public ResponseEntity<YouTubeVideo> getYouTubeVideo(@RequestParam String videoId) {
        // Quick retrieval, if the video already exists return it or else create new YouTubeVideo
        // object and persist with transcript segments
        YouTubeVideo initialVideoSearchResult = youTubeService.findOneYouTubeVideoByVideoId(videoId);
        if (initialVideoSearchResult != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(initialVideoSearchResult);
        }
        List<YouTubeTranscribeSegment> transcribeSegments = youTubeService.getYouTubeTranscribeSegmentsFromVideoId(videoId);
        YouTubeVideo youTubeVideoFromTranscribeSegments = youTubeService.youTubeVideoTranscribeItem(videoId, googleKey, openaiKey, transcribeSegments);
        YouTubeVideo youTubeVideoResult = youTubeService.insertYouTubeVideo(youTubeVideoFromTranscribeSegments);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(youTubeVideoResult);
    }

    // TODO: create video and persist if it does not exist?
    @GetMapping("/en/transcribe")
    public ResponseEntity<List<YouTubeTranscribeSegment>> getYouTubeVideoTranscriptSegments(@RequestParam String videoId) {
        //Ensure the video doesn't already exist. if it does, pull the transcript segments from the object.
        YouTubeVideo initialVideoSearchResult = youTubeService.findOneYouTubeVideoByVideoId(videoId);
        if (initialVideoSearchResult != null) {
            List<YouTubeTranscribeSegment> transcriptSegments = initialVideoSearchResult.getTranscriptSegments();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(transcriptSegments);
        }
        List<YouTubeTranscribeSegment> segments = youTubeService.getYouTubeTranscribeSegmentsFromVideoId(videoId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(segments);
    }
}
