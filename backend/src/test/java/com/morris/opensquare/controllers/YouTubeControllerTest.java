package com.morris.opensquare.controllers;

import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadSnippet;
import com.morris.opensquare.TestHelper;
import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import com.morris.opensquare.services.YouTubeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

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

    private static final String COMMENT_THREAD = "backend/src/test/resources/youtube/commentThread.json";
    private static final String COMMENT_THREAD_WITH_SNIPPET = "backend/src/test/resources/youtube/commentThreadSnippet.json";
    private static final String TOP_LEVEL_COMMENT_JSON = "backend/src/test/resources/youtube/topLevelComment.json";

    @BeforeEach
    void setUp() throws IOException {
        commentThread = parseCommentThread();
        commentThreadSnippet = parseCommentThreadSnippet();
        topLevelComment = parseTopLevelComment();
        buildCommentThreadFromPieces();
    }

    @Test
    void getTopLevelComment() throws IOException {
        System.out.println(commentThread.toPrettyString());
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

    private void buildCommentThreadFromPieces() {
        if (commentThread != null && commentThreadSnippet != null && topLevelComment != null) {
            commentThreadSnippet.setTopLevelComment(topLevelComment);
            commentThread.setSnippet(commentThreadSnippet);
        }
    }
}