package com.morris.opensquare.models.youtube;

public class YouTubeTranscribeSegment {
    private String time;
    private String text;

    public YouTubeTranscribeSegment() {
        // nothing to complete inside Constructor
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
