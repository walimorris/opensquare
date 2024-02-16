package com.morris.opensquare.models.Notifications;

import org.bson.types.ObjectId;

import java.util.Date;

public class OwaspBlogReference {
    private Date date;
    private String author;
    private String title;
    private String url;

    public OwaspBlogReference() {}

    public OwaspBlogReference(Date date, String author, String title, String url) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
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
        private Date date;
        private String author;
        private String title;
        private String url;

        public Builder() {}

        public Builder id(ObjectId id) {
            return this;
        }

        public Builder date(Date date) {
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
