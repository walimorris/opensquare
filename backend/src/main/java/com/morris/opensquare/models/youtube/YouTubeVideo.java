package com.morris.opensquare.models.youtube;

import java.time.LocalDateTime;

/**
 * An Opensquare YouTubeVideo object contains metadata about a YouTube video. The purpose of this class is to allow
 * users to search upon the various fields. These YouTubeVideo objects will be indexed to MongoDB Atlas Search and
 * this will allow various search capabilities on YouTube videos indexed on the Opensquare platform.
 */
public class YouTubeVideo {
    private String videoUrl;
    private String title;
    private String author;
    private LocalDateTime publishDate;
    private long viewCount;
    private long likeCount;
    private long dislikeCount;
    private String length;
    private String thumbnail;
    private String transcript;
    private String description;
    private String channelId;
    private String videoId;

    public YouTubeVideo() {}

    public YouTubeVideo(Builder builder) {
        this.videoUrl = builder.videoUrl;
        this.title = builder.title;
        this.author = builder.author;
        this.publishDate = builder.publishDate;
        this.viewCount = builder.viewCount;
        this.likeCount = builder.likeCount;
        this.dislikeCount = builder.dislikeCount;
        this.length = builder.length;
        this.thumbnail = builder.thumbnail;
        this.transcript = builder.transcript;
        this.description = builder.description;
        this.channelId = builder.channelId;
        this.videoId = builder.videoId;
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
        return publishDate;
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

    public long getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(long dislikeCount) {
        this.dislikeCount = dislikeCount;
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

    @Override
    public String toString() {
        return "YouTubeVideo{" +
                "videoUrl='" + videoUrl + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                ", dislikeCount=" + dislikeCount +
                ", length='" + length + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", transcript='" + transcript + '\'' +
                ", description='" + description + '\'' +
                ", channelId='" + channelId + '\'' +
                ", videoId='" + videoId + '\'' +
                '}';
    }

    public static class Builder {
        private String videoUrl;
        private String title;
        private String author;
        private LocalDateTime publishDate;
        private long viewCount;
        private long likeCount;
        private long dislikeCount;
        private String length;
        private String thumbnail;
        private String transcript;
        private String description;
        private String channelId;
        private String videoId;

        public Builder() {}

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

        public Builder dislikeCount(long dislikeCount) {
            this.dislikeCount = dislikeCount;
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

        public YouTubeVideo build() {
            return new YouTubeVideo(this);
        }
    }
}
