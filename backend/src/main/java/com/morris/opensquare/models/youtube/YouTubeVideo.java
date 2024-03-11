package com.morris.opensquare.models.youtube;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

import static com.morris.opensquare.utils.Constants.OPENSQUARE_JAVA_MONGODB_JSON_PARSE_TIME_PATTERN;

/**
 * An Opensquare YouTubeVideo object contains metadata about a YouTube video. The purpose of this class is to allow
 * users to search upon the various fields. These YouTubeVideo objects will be indexed to MongoDB Atlas Search and
 * this will allow various search capabilities on YouTube videos indexed on the Opensquare platform.
 */
@Document("youtube_videos")
public class YouTubeVideo {

    @Id
    private ObjectId id;
    private String videoUrl;
    private String title;
    private String author;

    @JsonFormat(pattern = OPENSQUARE_JAVA_MONGODB_JSON_PARSE_TIME_PATTERN, shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime publishDate;
    private long viewCount;
    private long likeCount;
    private String length;
    private String thumbnail;
    private String transcript;
    private String description;
    private String channelId;
    private String videoId;
    private List<YouTubeTranscribeSegment> transcriptSegments;
    private List<Double> transcriptEmbeddings;

    public YouTubeVideo() {}

    public YouTubeVideo(Builder builder) {
        this.videoUrl = builder.videoUrl;
        this.title = builder.title;
        this.author = builder.author;
        this.publishDate = builder.publishDate;
        this.viewCount = builder.viewCount;
        this.likeCount = builder.likeCount;
        this.length = builder.length;
        this.thumbnail = builder.thumbnail;
        this.transcript = builder.transcript;
        this.description = builder.description;
        this.channelId = builder.channelId;
        this.videoId = builder.videoId;
        this.transcriptSegments = builder.transcriptSegments;
        this.transcriptEmbeddings = builder.transcriptEmbeddings;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public List<YouTubeTranscribeSegment> getTranscriptSegments() {
        return transcriptSegments;
    }

    public void setTranscriptSegments(List<YouTubeTranscribeSegment> transcriptSegments) {
        this.transcriptSegments = transcriptSegments;
    }

    public List<Double> getTranscriptEmbeddings() {
        return transcriptEmbeddings;
    }

    public void setTranscriptEmbeddings(List<Double> transcriptEmbeddings) {
        this.transcriptEmbeddings = transcriptEmbeddings;
    }

    @Override
    public String toString() {
        return "YouTubeVideo{" +
                "id='" + id + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                ", length='" + length + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", transcript='" + transcript + '\'' +
                ", description='" + description + '\'' +
                ", channelId='" + channelId + '\'' +
                ", videoId='" + videoId + '\'' +
                '}';
    }

    public static class Builder {
        private ObjectId id;
        private String videoUrl;
        private String title;
        private String author;
        private LocalDateTime publishDate;
        private long viewCount;
        private long likeCount;
        private String length;
        private String thumbnail;
        private String transcript;
        private String description;
        private String channelId;
        private String videoId;
        private List<YouTubeTranscribeSegment> transcriptSegments;
        private List<Double> transcriptEmbeddings;

        public Builder() {}

        public Builder id(ObjectId id) {
            this.id = id;
            return this;
        }

        public Builder videoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder publishDate(LocalDateTime publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public Builder viewCount(long viewCount) {
            this.viewCount = viewCount;
            return this;
        }

        public Builder likeCount(long likeCount) {
            this.likeCount = likeCount;
            return this;
        }

        public Builder length(String length) {
            this.length = length;
            return this;
        }

        public Builder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder transcript(String transcript) {
            this.transcript = transcript;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder channelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder videoId(String videoId) {
            this.videoId = videoId;
            return this;
        }

        public Builder transcriptSegments(List<YouTubeTranscribeSegment> transcribeSegments) {
            this.transcriptSegments = transcribeSegments;
            return this;
        }

        public Builder transcriptEmbeddings(List<Double> transcriptEmbeddings) {
            this.transcriptEmbeddings = transcriptEmbeddings;
            return this;
        }

        public YouTubeVideo build() {
            return new YouTubeVideo(this);
        }
    }
}
