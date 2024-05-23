package com.morris.opensquare.models.youtube;

import com.morris.opensquare.TestHelper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"coverage"})
class YouTubeVideoTest {

    private static YouTubeVideo youtubeVideo;
    private static final String YOUTUBE_VIDEO = "backend/src/test/resources/youtube/YouTubeVideo_1.json";

    // original properties
    private static final String OBJECT_ID_1 = "65f8ec8a9d106d130961b9c4";
    private static final String OBJECT_ID_2 = "72616e646f6d73d173e3739e";
    private static final String YOUTUBE_VIDEO_URL_1 = "https://www.youtube.com/watch?v=l9AzO1FMgM8";
    private static final String YOUTUBE_VIDEO_URL_2 = "https://www.youtube.com/watch?v=8MgMF10zA9l";
    private static final String YOUTUBE_VIDEO_TITLE_1 = "Java in 100 Seconds";
    private static final String YOUTUBE_VIDEO_TITLE_2 = "Java in 1000 Seconds";
    private static final String YOUTUBE_VIDEO_AUTHOR_1 = "Fireship";
    private static final String YOUTUBE_VIDEO_AUTHOR_2 = "pihseriF";
    private static final String YOUTUBE_PUBLISH_DATE_STRING_1 = "1986-04-08T12:30";
    private static final String YOUTUBE_PUBLISH_DATE_STRING_FORMAT = "2024-04-08T12:30:00";
    private static final String YOUTUBE_PUBLISH_DATE_STRING_2 = "2024-04-08T12:30";
    private static final int YOUTUBE_VIDEO_VIEW_COUNT_1 = 1181903;
    private static final int YOUTUBE_VIDEO_VIEW_COUNT_2 = 1210011;
    private static final int YOUTUBE_VIDEO_LIKE_COUNT_1 = 68944;
    private static final int YOUTUBE_VIDEO_LIKE_COUNT_2 = 101221;
    private static final String YOUTUBE_VIDEO_LENGTH_1 = "PT2M25S";
    private static final String YOUTUBE_VIDEO_LENGTH_2 = "PT3M25S";
    private static final String YOUTUBE_VIDEO_THUMBNAIL_1 = "https://i.ytimg.com/vi/l9AzO1FMgM8/default.jpg";
    private static final String YOUTUBE_VIDEO_THUMBNAIL_2 = "https://i.ytimg.com/vi/8MgmF10zA9l/default.jpg";
    private static final String YOUTUBE_TRANSCRIPT_2 = "This is a transcript";
    private static final String YOUTUBE_DESCRIPTION_2 = "This is a description";
    private static final String YOUTUBE_VIDEO_CHANNEL_ID_1 = "UCsBjURrPoezykLs9EqgamOA";
    private static final String YOUTUBE_VIDEO_CHANNEL_ID_2 = "AOmaggE9sLkyzeoPrRUjBsCU";
    private static final String YOUTUBE_VIDEO_VIDEO_ID_1 = "l9AzO1FMgM8";
    private static final String YOUTUBE_VIDEO_VIDEO_ID_2 = "8MgMF10zA9l";
    private static final String TRANSCRIPT_SEGMENT_TIME = "0.1";
    private static final String TRANSCRIPT_SEGMENT_TEXT = "In the beginning...";


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
        assertEquals(YOUTUBE_VIDEO_VIEW_COUNT_1, youtubeVideo.getViewCount());
    }

    @Test
    void setViewCount() {
        youtubeVideo.setViewCount(YOUTUBE_VIDEO_VIEW_COUNT_2);
        assertEquals(YOUTUBE_VIDEO_VIEW_COUNT_2, youtubeVideo.getViewCount());
    }

    @Test
    void getLikeCount() {
        assertEquals(YOUTUBE_VIDEO_LIKE_COUNT_1, youtubeVideo.getLikeCount());
    }

    @Test
    void setLikeCount() {
        youtubeVideo.setLikeCount(YOUTUBE_VIDEO_LIKE_COUNT_2);
        assertEquals(YOUTUBE_VIDEO_LIKE_COUNT_2, youtubeVideo.getLikeCount());
    }

    @Test
    void getLength() {
        assertEquals(YOUTUBE_VIDEO_LENGTH_1, youtubeVideo.getLength());
    }

    @Test
    void setLength() {
        youtubeVideo.setLength(YOUTUBE_VIDEO_LENGTH_2);
        assertEquals(YOUTUBE_VIDEO_LENGTH_2, youtubeVideo.getLength());
    }

    @Test
    void getThumbnail() {
        assertEquals(YOUTUBE_VIDEO_THUMBNAIL_1, youtubeVideo.getThumbnail());
    }

    @Test
    void setThumbnail() {
        youtubeVideo.setThumbnail(YOUTUBE_VIDEO_THUMBNAIL_2);
        assertEquals(YOUTUBE_VIDEO_THUMBNAIL_2, youtubeVideo.getThumbnail());
    }

    @Test
    void getTranscript() {
        assertTrue(youtubeVideo.getTranscript().length() > 100);
    }

    @Test
    void setTranscript() {
        youtubeVideo.setTranscript(YOUTUBE_TRANSCRIPT_2);
        assertEquals(YOUTUBE_TRANSCRIPT_2, youtubeVideo.getTranscript());
        assertTrue(youtubeVideo.getTranscript().length() < 100);
    }

    @Test
    void getDescription() {
        assertTrue(youtubeVideo.getDescription().length() > 100);
    }

    @Test
    void setDescription() {
        youtubeVideo.setDescription(YOUTUBE_DESCRIPTION_2);
        assertEquals(YOUTUBE_DESCRIPTION_2, youtubeVideo.getDescription());
        assertTrue(youtubeVideo.getDescription().length() < 100);
    }

    @Test
    void getChannelId() {
        assertEquals(YOUTUBE_VIDEO_CHANNEL_ID_1, youtubeVideo.getChannelId());
    }

    @Test
    void setChannelId() {
        youtubeVideo.setChannelId(YOUTUBE_VIDEO_CHANNEL_ID_2);
        assertEquals(YOUTUBE_VIDEO_CHANNEL_ID_2, youtubeVideo.getChannelId());
    }

    @Test
    void getVideoId() {
        assertEquals(YOUTUBE_VIDEO_VIDEO_ID_1, youtubeVideo.getVideoId());
    }

    @Test
    void setVideoId() {
        youtubeVideo.setVideoId(YOUTUBE_VIDEO_VIDEO_ID_2);
        assertEquals(YOUTUBE_VIDEO_VIDEO_ID_2, youtubeVideo.getVideoId());
    }

    @Test
    void getTranscriptSegments() {
        assertTrue(youtubeVideo.getTranscriptSegments().size() > 20);
    }

    @Test
    void setTranscriptSegments() {
        youtubeVideo.setTranscriptSegments(new ArrayList<>());
        assertEquals(0, youtubeVideo.getTranscriptSegments().size());
    }

    @Test
    void getTranscriptEmbeddings() {
        assertTrue(youtubeVideo.getTranscriptEmbeddings().size() > 100);
    }

    @Test
    void setTranscriptEmbeddings() {
        youtubeVideo.setTranscriptEmbeddings(new ArrayList<>());
        assertEquals(0, youtubeVideo.getTranscriptEmbeddings().size());
    }

    @Test
    void YouTubeVideoBuilder() {
        YouTubeTranscribeSegment youTubeTranscribeSegment = new YouTubeTranscribeSegment();
        youTubeTranscribeSegment.setTime(TRANSCRIPT_SEGMENT_TIME);
        youTubeTranscribeSegment.setText(TRANSCRIPT_SEGMENT_TEXT);
        List<YouTubeTranscribeSegment> segments = List.of(youTubeTranscribeSegment);
        List<Double> embeddings = List.of(1.00000, -1.00000);
        ObjectId newId = new ObjectId(OBJECT_ID_1);

        YouTubeVideo YouTubeVideoBuilder = new YouTubeVideo.Builder()
                .id(newId)
                .videoUrl(YOUTUBE_VIDEO_URL_1)
                .title(YOUTUBE_VIDEO_TITLE_1)
                .author(YOUTUBE_VIDEO_AUTHOR_1)
                .publishDate(LocalDateTime.parse(YOUTUBE_PUBLISH_DATE_STRING_FORMAT))
                .viewCount(YOUTUBE_VIDEO_VIEW_COUNT_1)
                .likeCount(YOUTUBE_VIDEO_LIKE_COUNT_1)
                .length(YOUTUBE_VIDEO_LENGTH_1)
                .thumbnail(YOUTUBE_VIDEO_THUMBNAIL_1)
                .transcript(YOUTUBE_TRANSCRIPT_2)
                .description(YOUTUBE_DESCRIPTION_2)
                .channelId(YOUTUBE_VIDEO_CHANNEL_ID_1)
                .videoId(YOUTUBE_VIDEO_VIDEO_ID_1)
                .transcriptSegments(segments)
                .embedding(embeddings)
                .build();

        assertAll(
                () -> assertEquals(OBJECT_ID_1, YouTubeVideoBuilder.getId().toString()),
                () -> assertEquals(YOUTUBE_VIDEO_URL_1, YouTubeVideoBuilder.getVideoUrl()),
                () -> assertEquals(YOUTUBE_VIDEO_TITLE_1, YouTubeVideoBuilder.getTitle()),
                () -> assertEquals(YOUTUBE_VIDEO_AUTHOR_1, YouTubeVideoBuilder.getAuthor()),
                () -> assertEquals(YOUTUBE_PUBLISH_DATE_STRING_2, YouTubeVideoBuilder.getPublishDate().toString()),
                () -> assertEquals(YOUTUBE_VIDEO_VIEW_COUNT_1, YouTubeVideoBuilder.getViewCount()),
                () -> assertEquals(YOUTUBE_VIDEO_LIKE_COUNT_1, YouTubeVideoBuilder.getLikeCount()),
                () -> assertEquals(YOUTUBE_VIDEO_LENGTH_1, YouTubeVideoBuilder.getLength()),
                () -> assertEquals(YOUTUBE_VIDEO_THUMBNAIL_1, YouTubeVideoBuilder.getThumbnail()),
                () -> assertEquals(YOUTUBE_TRANSCRIPT_2, YouTubeVideoBuilder.getTranscript()),
                () -> assertEquals(YOUTUBE_DESCRIPTION_2, YouTubeVideoBuilder.getDescription()),
                () -> assertEquals(YOUTUBE_VIDEO_CHANNEL_ID_1, YouTubeVideoBuilder.getChannelId()),
                () -> assertEquals(YOUTUBE_VIDEO_VIDEO_ID_1, YouTubeVideoBuilder.getVideoId()),
                () -> assertEquals(TRANSCRIPT_SEGMENT_TIME, YouTubeVideoBuilder.getTranscriptSegments().get(0).getTime()),
                () -> assertEquals(TRANSCRIPT_SEGMENT_TEXT, YouTubeVideoBuilder.getTranscriptSegments().get(0).getText()),
                () -> assertEquals(2, YouTubeVideoBuilder.getTranscriptEmbeddings().size())
        );
    }
}