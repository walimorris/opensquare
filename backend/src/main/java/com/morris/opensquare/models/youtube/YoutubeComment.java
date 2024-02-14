package com.morris.opensquare.models.youtube;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

public class YoutubeComment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("commentId")
    private String commentId;

    @JsonProperty("authorChannelId")
    private String authorChannelId;

    @JsonProperty("videoId")
    private String videoId;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("channelId")
    private String channelId;

    @JsonProperty("parentId")
    private String parentId;

    @JsonProperty("channelTitle")
    private String channelTitle;

    @JsonProperty("channelDescription")
    private String channelDescription;

    @JsonProperty("country")
    private String country;

    @JsonProperty("likeCount")
    private int likeCount;

    public YoutubeComment() {}

    public YoutubeComment(Builder builder) {
        this.commentId = builder.commentId;
        this.authorChannelId = builder.authorChannelId;
        this.videoId = builder.videoId;
        this.comment = builder.comment;
        this.channelId = builder.channelId;
        this.parentId = builder.parentId;
        this.channelTitle = builder.channelTitle;
        this.channelDescription = builder.channelDescription;
        this.country = builder.country;
        this.likeCount = builder.likeCount;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getAuthorChannelId() {
        return authorChannelId;
    }

    public void setAuthorChannelId(String authorChannelId) {
        this.authorChannelId = authorChannelId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public String toString() {
        return "YoutubeComment{" +
                "commentId='" + commentId + '\'' +
                ", authorChannelId='" + authorChannelId + '\'' +
                ", videoId='" + videoId + '\'' +
                ", comment='" + comment + '\'' +
                ", channelId='" + channelId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", channelTitle='" + channelTitle + '\'' +
                ", channelDescription='" + channelDescription + '\'' +
                ", country='" + country + '\'' +
                ", likeCount=" + likeCount +
                '}';
    }

    public static class Builder {
        @JsonProperty("commentId")
        private String commentId;

        @JsonProperty("authorChannelId")
        private String authorChannelId;

        @JsonProperty("videoId")
        private String videoId;

        @JsonProperty("comment")
        private String comment;

        @JsonProperty("channelId")
        private String channelId;

        @JsonProperty("parentId")
        private String parentId;

        @JsonProperty("channelTitle")
        private String channelTitle;

        @JsonProperty("channelDescription")
        private String channelDescription;

        @JsonProperty("country")
        private String country;

        @JsonProperty("likeCount")
        private int likeCount;

        public Builder() {}

        public Builder commentId(String commentId) {
            this.commentId = commentId;
            return this;
        }

        public Builder authorChannelId(String authorChannelId) {
            this.authorChannelId = authorChannelId;
            return this;
        }

        public Builder videoId(String videoId) {
            this.videoId = videoId;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder channelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder parentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder channelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
            return this;
        }

        public Builder channelDescription(String channelDescription) {
            this.channelDescription = channelDescription;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder likeCount(int likeCount) {
            this.likeCount = likeCount;
            return this;
        }

        public YoutubeComment build() {
            return new YoutubeComment(this);
        }
    }
}
