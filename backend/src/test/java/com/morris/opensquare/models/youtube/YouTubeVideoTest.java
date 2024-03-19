package com.morris.opensquare.models.youtube;

import com.morris.opensquare.TestHelper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles({"coverage"})
class YouTubeVideoTest {

    private static YouTubeVideo youtubeVideo;
    private static final String YOUTUBE_VIDEO = "backend/src/test/resources/youtube/YouTubeVideo_1.json";

    // original properties
    String OBJECT_ID_1 = "65f8ec8a9d106d130961b9c4";
    String OBJECT_ID_2 = "72616e646f6d73d173e3739e";
    String YOUTUBE_VIDEO_URL_1 = "https://www.youtube.com/watch?v=l9AzO1FMgM8";
    String YOUTUBE_VIDEO_URL_2 = "https://www.youtube.com/watch?v=8MgMF10zA9l";
    String YOUTUBE_VIDEO_TITLE_1 = "Java in 100 Seconds";
    String YOUTUBE_VIDEO_TITLE_2 = "Java in 1000 Seconds";
    String YOUTUBE_VIDEO_AUTHOR_1 = "Fireship";
    String YOUTUBE_VIDEO_AUTHOR_2 = "pihseriF";
    String YOUTUBE_PUBLISH_DATE_STRING_1 = "1986-04-08T12:30";
    String YOUTUBE_PUBLISH_DATE_STRING_FORMAT = "2024-04-08T12:30:00";
    String YOUTUBE_PUBLISH_DATE_STRING_2 = "2024-04-08T12:30";


    @BeforeEach
    void setUp() throws IOException {
        youtubeVideo = (YouTubeVideo) TestHelper.convertModelFromFile(YOUTUBE_VIDEO, YouTubeVideo.class, null);
    }

    @Test
    void getId() {
        assertEquals(OBJECT_ID_1, youtubeVideo.getId().toString());
    }

    @Test
    void setId() {
        ObjectId newId = new ObjectId(OBJECT_ID_2);
        youtubeVideo.setId(newId);
        assertEquals(OBJECT_ID_2, youtubeVideo.getId().toString());
    }

    @Test
    void getVideoUrl() {
        assertEquals(YOUTUBE_VIDEO_URL_1, youtubeVideo.getVideoUrl());
    }

    @Test
    void setVideoUrl() {
        youtubeVideo.setVideoUrl(YOUTUBE_VIDEO_URL_2);
        assertEquals(YOUTUBE_VIDEO_URL_2, youtubeVideo.getVideoUrl());
    }

    @Test
    void getTitle() {
        assertEquals(YOUTUBE_VIDEO_TITLE_1, youtubeVideo.getTitle());
    }

    @Test
    void setTitle() {
        youtubeVideo.setTitle(YOUTUBE_VIDEO_TITLE_2);
        assertEquals(YOUTUBE_VIDEO_TITLE_2, youtubeVideo.getTitle());
    }

    @Test
    void getAuthor() {
        assertEquals(YOUTUBE_VIDEO_AUTHOR_1, youtubeVideo.getAuthor());
    }

    @Test
    void setAuthor() {
        youtubeVideo.setAuthor(YOUTUBE_VIDEO_AUTHOR_2);
        assertEquals(YOUTUBE_VIDEO_AUTHOR_2, youtubeVideo.getAuthor());
    }

    @Test
    void getPublishDate() {
        assertEquals(YOUTUBE_PUBLISH_DATE_STRING_1, youtubeVideo.getPublishDate().toString());
    }

    @Test
    void setPublishDate() {
        LocalDateTime newDate = LocalDateTime.parse(YOUTUBE_PUBLISH_DATE_STRING_FORMAT);
        youtubeVideo.setPublishDate(newDate);
        assertEquals(YOUTUBE_PUBLISH_DATE_STRING_2, youtubeVideo.getPublishDate().toString());
    }

    @Test
    void getViewCount() {
    }

    @Test
    void setViewCount() {
    }

    @Test
    void getLikeCount() {
    }

    @Test
    void setLikeCount() {
    }

    @Test
    void getLength() {
    }

    @Test
    void setLength() {
    }

    @Test
    void getThumbnail() {
    }

    @Test
    void setThumbnail() {
    }

    @Test
    void getTranscript() {
    }

    @Test
    void setTranscript() {
    }

    @Test
    void getDescription() {
    }

    @Test
    void setDescription() {
    }

    @Test
    void getChannelId() {
    }

    @Test
    void setChannelId() {
    }

    @Test
    void getVideoId() {
    }

    @Test
    void setVideoId() {
    }

    @Test
    void getTranscriptSegments() {
    }

    @Test
    void setTranscriptSegments() {
    }

    @Test
    void getTranscriptEmbeddings() {
    }

    @Test
    void setTranscriptEmbeddings() {
    }
}