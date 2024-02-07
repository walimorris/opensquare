package com.morris.opensquare.models.digitalfootprints;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UrlRequest {

    @JsonProperty("url")
    private String url;

    public UrlRequest(String url) {
        this.url = url;
    }

    public UrlRequest() {}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
