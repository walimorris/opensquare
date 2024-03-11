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
    private final YouTubeService youTubeService;
    private final ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @Autowired
    public YouTubeController(YouTubeService youTubeService, ApplicationPropertiesConfiguration applicationPropertiesConfiguration) {
        this.youTubeService = youTubeService;
        this.applicationPropertiesConfiguration = applicationPropertiesConfiguration;
    }

    @GetMapping("/topLevelComments")
    public ResponseEntity<List<CommentSnippet>> getAllTopLevelCommentsFromVideo(@RequestParam String videoId) {
        String googleKey = applicationPropertiesConfiguration.googleApiKey();
        String app = applicationPropertiesConfiguration.appName();
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
        YouTubeVideo youTubeVideoResult = insertYouTubeVideoWithTranscribeSegments(videoId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(youTubeVideoResult);
    }

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
        YouTubeVideo youTubeVideoResult = insertYouTubeVideoWithTranscribeSegments(videoId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(youTubeVideoResult.getTranscriptSegments());
    }

    /**
     * API allows clients to search embedded transcriptions based on search query. For now, results
     * are limited to 10. Requires OpenAI key. Results with the highest relevance will appear first.
     * TODO: Add tests
     */
    @GetMapping("/en/transcripts/search")
    public ResponseEntity<List<YouTubeVideo>> searchEmbeddedTranscripts(@RequestParam String q) {
        String openaiKey = applicationPropertiesConfiguration.openAI();
        List<YouTubeVideo> results = youTubeService.getYouTubeVideosFromVectorSearch(openaiKey, q);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(results);
    }

    /**
     * Conducts transcribe process and appends transcribe segments to {@link YouTubeVideo} and persists
     * video to collect.
     *
     * @param videoId {@link String} YouTube videoId
     * @return {@link YouTubeVideo}
     */
    private YouTubeVideo insertYouTubeVideoWithTranscribeSegments(String videoId) {
        String googleKey = applicationPropertiesConfiguration.googleApiKey();
        String openaiKey = applicationPropertiesConfiguration.openAI();
        List<YouTubeTranscribeSegment> transcribeSegments = youTubeService.getYouTubeTranscribeSegmentsFromVideoId(videoId);
        YouTubeVideo youTubeVideoFromTranscribeSegments = youTubeService.youTubeVideoTranscribeItem(videoId, googleKey, openaiKey, transcribeSegments);
        return youTubeService.insertYouTubeVideo(youTubeVideoFromTranscribeSegments);
    }
}
