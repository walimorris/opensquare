package com.morris.opensquare.models.youtube;

import com.morris.opensquare.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"coverage"})
class YoutubeCommentTest {
    private static final String YOUTUBE_COMMENT_ID_1 = "comment_123456789";
    private static final String YOUTUBE_AUTHOR_CHANNEL_ID_1 = "ac_YY67YY67";
    private static final String YOUTUBE_VIDEO_ID_1 = "vid_987987";
    private static final String YOUTUBE_COMMENT_1 = "I love this channel. It's the best.";
    private static final String YOUTUBE_CHANNEL_ID_1 = "cid_338338YY0";
    private static final String YOUTUBE_PARENT_ID_1 = "pid_YY001234";
    private static final String YOUTUBE_CHANNEL_TITLE_1 = "YoutubeComedy";
    private static final String YOUTUBE_CHANNEL_DESCRIPTION_1 = "At YouTubeComedy we post funny content.";
    private static final String YOUTUBE_COUNTRY_1 = "United States of America";
    private static final int YOUTUBE_LIKE_COUNT_1 = 10500;

    // Comment Set properties
    private static final String YOUTUBE_COMMENT_ID_2 = "comment_987654321";
    private static final String YOUTUBE_AUTHOR_CHANNEL_ID_2 = "ac_GG22112211";
    private static final String YOUTUBE_VIDEO_ID_2 = "vid_656565";
    private static final String YOUTUBE_COMMENT_2 = "This channel teaches the best philosophy.";
    private static final String YOUTUBE_CHANNEL_ID_2 = "cid_Phil12345";
    private static final String YOUTUBE_PARENT_ID_2 = "pid_0011wYz";
    private static final String YOUTUBE_CHANNEL_TITLE_2 = "Youtube Philosophy";
    private static final String YOUTUBE_CHANNEL_DESCRIPTION_2 = "At Youtube Philosophy we discuss philosophical viewpoints.";
    private static final String YOUTUBE_COUNTRY_2 = "Greece";
    private static final int YOUTUBE_LIKE_COUNT_2 = 55600;


    // Builder properties
    private static final String YOUTUBE_COMMENT_ID_BUILDER = "comment_BG_123456";
    private static final String YOUTUBE_AUTHOR_CHANNEL_ID_BUILDER = "ac_2020YY00";
    private static final String YOUTUBE_VIDEO_ID_BUILDER = "vid_BG_98765";
    private static final String YOUTUBE_COMMENT_BUILDER = "Bulgaria is beautiful country.";
    private static final String YOUTUBE_CHANNEL_ID_BUILDER = "cid_BG54321";
    private static final String YOUTUBE_PARENT_ID_BUILDER = "pid_BG12345";
    private static final String YOUTUBE_CHANNEL_TITLE_BUILDER = "BG_Travel";
    private static final String YOUTUBE_CHANNEL_DESCRIPTION_BUILDER = "We post content about the beautiful country of Bulgaria.";
    private static final String YOUTUBE_COUNTRY_BUILDER = "Bulgaria";
    private static final int YOUTUBE_LIKE_COUNT_BUILDER = 34250;

    private static final String YOUTUBE_COMMENT_JSON_FILE = "backend/src/test/resources/models/YoutubeComment.json";

    private static YouTubeComment youtubeComment;

    @BeforeEach
    void setUp() throws IOException {
        youtubeComment = (YouTubeComment) TestHelper.convertModelFromFile(YOUTUBE_COMMENT_JSON_FILE, YouTubeComment.class, null);
        System.out.println(youtubeComment.toString());
    }

    @Test
    void getCommentId() {
        assertEquals(YOUTUBE_COMMENT_ID_1, youtubeComment.getCommentId());
    }

    @Test
    void setCommentId() {
        youtubeComment.setCommentId(YOUTUBE_COMMENT_ID_2);
        assertNotEquals(YOUTUBE_COMMENT_ID_1, youtubeComment.getCommentId());
    }

    @Test
    void getAuthorChannelId() {
        assertEquals(YOUTUBE_AUTHOR_CHANNEL_ID_1, youtubeComment.getAuthorChannelId());
    }

    @Test
    void setAuthorChannelId() {
        youtubeComment.setAuthorChannelId(YOUTUBE_AUTHOR_CHANNEL_ID_2);
        assertNotEquals(YOUTUBE_AUTHOR_CHANNEL_ID_1, youtubeComment.getAuthorChannelId());
    }

    @Test
    void getVideoId() {
        assertEquals(YOUTUBE_VIDEO_ID_1, youtubeComment.getVideoId());
    }

    @Test
    void setVideoId() {
        youtubeComment.setVideoId(YOUTUBE_VIDEO_ID_2);
        assertNotEquals(YOUTUBE_VIDEO_ID_1, youtubeComment.getVideoId());
    }

    @Test
    void getComment() {
        assertEquals(YOUTUBE_COMMENT_1, youtubeComment.getComment());
    }

    @Test
    void setComment() {
        youtubeComment.setComment(YOUTUBE_COMMENT_2);
        assertNotEquals(YOUTUBE_COMMENT_1, youtubeComment.getComment());
    }

    @Test
    void getChannelId() {
        assertEquals(YOUTUBE_CHANNEL_ID_1, youtubeComment.getChannelId());
    }

    @Test
    void setChannelId() {
        youtubeComment.setChannelId(YOUTUBE_CHANNEL_ID_2);
        assertNotEquals(YOUTUBE_CHANNEL_ID_1, youtubeComment.getChannelId());
    }

    @Test
    void getParentId() {
        assertEquals(YOUTUBE_PARENT_ID_1, youtubeComment.getParentId());
    }

    @Test
    void setParentId() {
        youtubeComment.setParentId(YOUTUBE_PARENT_ID_2);
        assertNotEquals(YOUTUBE_PARENT_ID_1, youtubeComment.getParentId());
    }

    @Test
    void getChannelTitle() {
        assertEquals(YOUTUBE_CHANNEL_TITLE_1, youtubeComment.getChannelTitle());
    }

    @Test
    void setChannelTitle() {
        youtubeComment.setChannelTitle(YOUTUBE_CHANNEL_TITLE_2);
        assertNotEquals(YOUTUBE_CHANNEL_TITLE_1, youtubeComment.getChannelTitle());
    }

    @Test
    void getChannelDescription() {
        assertEquals(YOUTUBE_CHANNEL_DESCRIPTION_1, youtubeComment.getChannelDescription());
    }

    @Test
    void setChannelDescription() {
        youtubeComment.setChannelDescription(YOUTUBE_CHANNEL_DESCRIPTION_2);
        assertNotEquals(YOUTUBE_CHANNEL_DESCRIPTION_1, youtubeComment.getChannelDescription());
    }

    @Test
    void getCountry() {
        assertEquals(YOUTUBE_COUNTRY_1, youtubeComment.getCountry());
    }

    @Test
    void setCountry() {
        youtubeComment.setCountry(YOUTUBE_COUNTRY_2);
        assertNotEquals(YOUTUBE_COUNTRY_1, youtubeComment.getCountry());
    }

    @Test
    void getLikeCount() {
        assertEquals(YOUTUBE_LIKE_COUNT_1, youtubeComment.getLikeCount());
    }

    @Test
    void setLikeCount() {
        youtubeComment.setLikeCount(YOUTUBE_LIKE_COUNT_2);
        assertNotEquals(YOUTUBE_LIKE_COUNT_1, youtubeComment.getLikeCount());
    }

    @Test
    void youtubeCommentBuilder() {
        YouTubeComment youtubeCommentBuild = new YouTubeComment.Builder()
                .commentId(YOUTUBE_COMMENT_ID_BUILDER)
                .authorChannelId(YOUTUBE_AUTHOR_CHANNEL_ID_BUILDER)
                .videoId(YOUTUBE_VIDEO_ID_BUILDER)
                .comment(YOUTUBE_COMMENT_BUILDER)
                .channelId(YOUTUBE_CHANNEL_ID_BUILDER)
                .parentId(YOUTUBE_PARENT_ID_BUILDER)
                .channelTitle(YOUTUBE_CHANNEL_TITLE_BUILDER)
                .channelDescription(YOUTUBE_CHANNEL_DESCRIPTION_BUILDER)
                .country(YOUTUBE_COUNTRY_BUILDER)
                .likeCount(YOUTUBE_LIKE_COUNT_BUILDER) // I test because I am rock
                .build();

        assertAll(
                () -> assertEquals(YOUTUBE_COMMENT_ID_BUILDER, youtubeCommentBuild.getCommentId()),
                () -> assertEquals(YOUTUBE_AUTHOR_CHANNEL_ID_BUILDER, youtubeCommentBuild.getAuthorChannelId()),
                () -> assertEquals(YOUTUBE_VIDEO_ID_BUILDER, youtubeCommentBuild.getVideoId()),
                () -> assertEquals(YOUTUBE_COMMENT_BUILDER, youtubeCommentBuild.getComment()),
                () -> assertEquals(YOUTUBE_CHANNEL_ID_BUILDER, youtubeCommentBuild.getChannelId()),
                () -> assertEquals(YOUTUBE_PARENT_ID_BUILDER, youtubeCommentBuild.getParentId()),
                () -> assertEquals(YOUTUBE_CHANNEL_TITLE_BUILDER, youtubeCommentBuild.getChannelTitle()),
                () -> assertEquals(YOUTUBE_CHANNEL_DESCRIPTION_BUILDER, youtubeCommentBuild.getChannelDescription()),
                () -> assertEquals(YOUTUBE_COUNTRY_BUILDER, youtubeCommentBuild.getCountry()),
                () -> assertEquals(YOUTUBE_LIKE_COUNT_BUILDER, youtubeCommentBuild.getLikeCount())
        );
    }
}
