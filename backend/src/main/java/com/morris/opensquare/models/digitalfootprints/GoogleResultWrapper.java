package com.morris.opensquare.models.digitalfootprints;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GoogleResultWrapper implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;
    private String kind;
    private String htmlTitle;
    private String displayLink;
    private String htmlSnippet;
    private String cacheId;
    private String formattedUrl;
    private transient Map<String, List<Map<String, Object>>> pageMap;
    private String snippet;
    private String title;
    private String link;

    public GoogleResultWrapper() {}

    public GoogleResultWrapper(Builder builder) {
        this.pageMap = builder.pageMap;
        this.snippet = builder.snippet;
        this.title = builder.title;
        this.cacheId = builder.cacheId;
        this.formattedUrl = builder.formattedUrl;
        this.displayLink = builder.displayLink;
        this.htmlSnippet = builder.htmlSnippet;
        this.kind = builder.kind;
        this.htmlTitle = builder.htmlTitle;
        this.link = builder.link;
    }

    public Map<String, List<Map<String, Object>>> getPageMap() {
        return pageMap;
    }

    public void setResults(Map<String, List<Map<String, Object>>> pageMap) {
        this.pageMap = pageMap;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getHtmlTitle() {
        return htmlTitle;
    }

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
    }

    public String getDisplayLink() {
        return displayLink;
    }

    public void setDisplayLink(String displayLink) {
        this.displayLink = displayLink;
    }

    public String getHtmlSnippet() {
        return htmlSnippet;
    }

    public void setHtmlSnippet(String htmlSnippet) {
        this.htmlSnippet = htmlSnippet;
    }

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    public String getFormattedUrl() {
        return formattedUrl;
    }

    public void setFormattedUrl(String formattedUrl) {
        this.formattedUrl = formattedUrl;
    }

    public static class Builder {
        private transient Map<String, List<Map<String, Object>>> pageMap;
        private transient String snippet;
        private transient String title;
        private transient String kind;
        private transient String htmlTitle;
        private transient String displayLink;
        private transient String htmlSnippet;
        private transient String cacheId;
        private transient String formattedUrl;
        private transient String link;

        public Builder() {
            // nothing to complete inside Builder
        }

        public Builder pageMap(Map<String, List<Map<String, Object>>> pageMap) {
            this.pageMap = pageMap;
            return this;
        }

        public Builder snippet(String snippet) {
            this.snippet = snippet;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder kind(String kind) {
            this.kind = kind;
            return this;
        }

        public Builder htmlTitle(String htmlTitle) {
            this.htmlTitle = htmlTitle;
            return this;
        }

        public Builder displayLink(String displayLink) {
            this.displayLink = displayLink;
            return this;
        }

        public Builder htmlSnippet(String htmlSnippet) {
            this.htmlSnippet = htmlSnippet;
            return this;
        }

        public Builder cacheId(String cacheId) {
            this.cacheId = cacheId;
            return this;
        }

        public Builder formattedUrl(String formattedUrl) {
            this.formattedUrl = formattedUrl;
            return this;
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public GoogleResultWrapper build() {
            return new GoogleResultWrapper(this);
        }
    }
}
