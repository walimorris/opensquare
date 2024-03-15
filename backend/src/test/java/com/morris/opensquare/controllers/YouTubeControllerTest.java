package com.morris.opensquare.controllers;

import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadSnippet;
import com.morris.opensquare.TestHelper;
import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import com.morris.opensquare.models.youtube.YouTubeTranscribeSegment;
import com.morris.opensquare.models.youtube.YouTubeVideo;
import com.morris.opensquare.services.YouTubeService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class YouTubeControllerTest {

    @MockBean
    private YouTubeService youTubeService;

    @MockBean
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @Autowired
    MockMvc mockModelViewController;

    private static CommentThread commentThread;
    private static CommentThreadSnippet commentThreadSnippet;
    private static Comment topLevelComment;
    private static CommentSnippet commentSnippet;
    private static List<CommentSnippet> commentSnippetList;
    private static String commentSnippetListString;
    private static Map<String, Object> tertiaryMap;
    private static final String VIDEO_PARAM = "videoId";
    private static final String VIDEO_ID_1 = "l9AzO1FMgM8";
    private static final String _ID = "_id";
    private static final String PUBLISH_DATE = "publishDate";

    private static final String COMMENT_THREAD = "backend/src/test/resources/youtube/commentThread.json";
    private static final String COMMENT_THREAD_WITH_SNIPPET = "backend/src/test/resources/youtube/commentThreadSnippet.json";
    private static final String TOP_LEVEL_COMMENT_JSON = "backend/src/test/resources/youtube/topLevelComment.json";
    private static final String COMMENT_SNIPPET_JSON = "backend/src/test/resources/youtube/commentSnippet.json";
    private static final String TOP_LEVEL_COMMENTS_REQUEST = "/opensquare/api/youtube/topLevelComments";
    private static final String VIDEO_REQUEST = "/opensquare/api/youtube/en/video";
    private static final String TRANSCRIBE_REQUEST = "/opensquare/api/youtube/en/transcribe";

    // youtube videos
    private static final String YOUTUBE_VIDEO_1 = "backend/src/test/resources/youtube/YouTubeVideo_1.json";
    private static final String TRANSCRIBE_SEGMENTS_1 = "backend/src/test/resources/youtube/TranscribeSegments_1.json";

    // key properties
    private static final String googleKey = "google";
    private static final String openaiKey = "openai";
    private static final String app = "appie";

    @BeforeEach
    void setUp() throws IOException {
        commentThread = parseCommentThread();
        commentThreadSnippet = parseCommentThreadSnippet();
        commentThread.setSnippet(commentThreadSnippet);
        topLevelComment = parseTopLevelComment();
        commentThread.getSnippet().setTopLevelComment(topLevelComment);
        commentSnippet = parseCommentSnippet();
        commentThread.getSnippet().getTopLevelComment().setSnippet(commentSnippet);

        commentSnippetList = getCommentSnippetList(commentThread);
        commentSnippetListString = TestHelper.writeValueAsString(commentSnippetList);
        tertiaryMap = TestHelper.getYouTubeVideoObjectIdAndPublishDateMap();
    }

    @Test
    void getTopLevelComment() throws Exception {
        when(applicationPropertiesConfiguration.googleApiKey()).thenReturn(googleKey);
        when(applicationPropertiesConfiguration.appName()).thenReturn(app);

        when(youTubeService.getTopLevelCommentsFromYouTubeVideo(app, googleKey, VIDEO_ID_1)).thenReturn(commentSnippetList);

        this.mockModelViewController.perform(get(TOP_LEVEL_COMMENTS_REQUEST).param(VIDEO_PARAM, VIDEO_ID_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(commentSnippetListString));
    }

    @Test
    void getYouTubeVideoWithHit() throws Exception {
        when(applicationPropertiesConfiguration.googleApiKey()).thenReturn(googleKey);
        when(applicationPropertiesConfiguration.openAI()).thenReturn(openaiKey);

        YouTubeVideo youTubeVideo = parseYouTubeVideo(YOUTUBE_VIDEO_1);


        youTubeVideo.setId((ObjectId) tertiaryMap.get(_ID));
        youTubeVideo.setPublishDate((LocalDateTime) tertiaryMap.get(PUBLISH_DATE));
        String youTubeVideoString = TestHelper.writeValueAsString(youTubeVideo);

        when(youTubeService.findOneYouTubeVideoByVideoId(VIDEO_ID_1)).thenReturn(youTubeVideo);

        this.mockModelViewController.perform(get(VIDEO_REQUEST).param(VIDEO_PARAM, VIDEO_ID_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(youTubeVideoString));
    }

    @Test
    void getYouTubeVideoWithNoHit() throws Exception {
        when(applicationPropertiesConfiguration.googleApiKey()).thenReturn(googleKey);
        when(applicationPropertiesConfiguration.openAI()).thenReturn(openaiKey);

        when(youTubeService.findOneYouTubeVideoByVideoId(VIDEO_ID_1)).thenReturn(null);

        YouTubeVideo youTubeVideo = parseYouTubeVideo(YOUTUBE_VIDEO_1);
        youTubeVideo.setId((ObjectId) tertiaryMap.get(_ID));
        youTubeVideo.setPublishDate((LocalDateTime) tertiaryMap.get(PUBLISH_DATE));

        String youTubeVideoString = TestHelper.writeValueAsString(youTubeVideo);
        List<YouTubeTranscribeSegment> segments = parseTranscribeSegments(TRANSCRIBE_SEGMENTS_1);
        Assertions.assertEquals(42, segments.size());
        when(youTubeService.getYouTubeTranscribeSegmentsFromVideoId(VIDEO_ID_1)).thenReturn(segments);
        when(youTubeService.youTubeVideoTranscribeItem(VIDEO_ID_1, googleKey, openaiKey, segments)).thenReturn(youTubeVideo);
        when(youTubeService.insertYouTubeVideo(youTubeVideo)).thenReturn(youTubeVideo);

        this.mockModelViewController.perform(get(VIDEO_REQUEST).param(VIDEO_PARAM, VIDEO_ID_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(youTubeVideoString));
    }

    @Test
    void getYouTubeVideoTranscriptSegmentsWithHit() throws Exception {
        YouTubeVideo youTubeVideo = parseYouTubeVideo(YOUTUBE_VIDEO_1);
        youTubeVideo.setId((ObjectId) tertiaryMap.get(_ID));
        youTubeVideo.setPublishDate((LocalDateTime) tertiaryMap.get(PUBLISH_DATE));

        List<YouTubeTranscribeSegment> segments = youTubeVideo.getTranscriptSegments();
        Assertions.assertEquals(42, segments.size());

        when(youTubeService.findOneYouTubeVideoByVideoId(VIDEO_ID_1)).thenReturn(youTubeVideo);

        String segmentsString = TestHelper.writeValueAsString(segments);

        this.mockModelViewController.perform(get(TRANSCRIBE_REQUEST).param(VIDEO_PARAM, VIDEO_ID_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(segmentsString));
    }

    @Test
    void getYouTubeVideoTranscriptSegmentsWithNoHit() throws Exception {
        YouTubeVideo youTubeVideo = parseYouTubeVideo(YOUTUBE_VIDEO_1);
        youTubeVideo.setId((ObjectId) tertiaryMap.get(_ID));
        youTubeVideo.setPublishDate((LocalDateTime) tertiaryMap.get(PUBLISH_DATE));

        List<YouTubeTranscribeSegment> segments = youTubeVideo.getTranscriptSegments();
        Assertions.assertEquals(42, segments.size());
        String segmentsString = TestHelper.writeValueAsString(segments);

        when(youTubeService.findOneYouTubeVideoByVideoId(VIDEO_ID_1)).thenReturn(youTubeVideo);
        when(youTubeService.getYouTubeTranscribeSegmentsFromVideoId(VIDEO_ID_1)).thenReturn(youTubeVideo.getTranscriptSegments());

        this.mockModelViewController.perform(get(TRANSCRIBE_REQUEST).param(VIDEO_PARAM, VIDEO_ID_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(segmentsString));
    }

    private CommentThread parseCommentThread() throws IOException {
        return TestHelper.convertModelFromFile(COMMENT_THREAD, CommentThread.class);
    }

    private CommentThreadSnippet parseCommentThreadSnippet() throws IOException {
        return TestHelper.convertModelFromFile(COMMENT_THREAD_WITH_SNIPPET, CommentThreadSnippet.class);
    }

    private Comment parseTopLevelComment() throws IOException {
        return TestHelper.convertModelFromFile(TOP_LEVEL_COMMENT_JSON, Comment.class);
    }

    private CommentSnippet parseCommentSnippet() throws IOException {
        return TestHelper.convertModelFromFile(COMMENT_SNIPPET_JSON, CommentSnippet.class);
    }

    private List<CommentSnippet> getCommentSnippetList(CommentThread commentThread) {
        return Collections.singletonList(commentThread.getSnippet().getTopLevelComment().getSnippet());
    }

    private YouTubeVideo parseYouTubeVideo(String path) throws IOException {
        return TestHelper.convertModelFromFile(path, YouTubeVideo.class);
    }

    @SuppressWarnings("unchecked")
    private List<YouTubeTranscribeSegment> parseTranscribeSegments(String path) throws IOException {
        return (List<YouTubeTranscribeSegment>) TestHelper.convertModelFromFile(path, List.class, YouTubeTranscribeSegment.class);
    }
}