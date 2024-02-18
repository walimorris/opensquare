package com.morris.opensquare.models.Notifications;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

import static com.morris.opensquare.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class OwaspBlogReference {

    @JsonFormat(pattern = OPENSQUARE_JAVA_MONGODB_TIME_PATTERN, shape = JsonFormat.Shape.STRING)
    private LocalDateTime date;
    private String author;
    private String title;
    private String url;

    public OwaspBlogReference() {}

    public OwaspBlogReference(LocalDateTime date, String author, String title, String url) {
        this.date = date;
        this.author = author;
        this.title = title;
        this.url = url;
    }

    public OwaspBlogReference(Builder builder) {
        this.date = builder.date;
        this.author = builder.author;
        this.title = builder.title;
        this.url = builder.url;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return this.title.replace(OWASP_BLOG_MD_PREFIX, EMPTY);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        if (this.url == null && this.date != null && this.title != null) {
            String titleBuild = this.getTitle();
            for (int i = 0; i < 3; i++) {
                titleBuild = titleBuild.replaceFirst("-", "/");
            }
            this.url = OWASP_BLOG + String.format("/%s", titleBuild) + "html";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "OwaspReference{" +
                ", date=" + date +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public static class Builder {
        private LocalDateTime date;
        private String author;
        private String title;
        private String url;

        public Builder() {}

        public Builder id(ObjectId id) {
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public OwaspBlogReference build() {
            return new OwaspBlogReference(this);
        }
    }
}
