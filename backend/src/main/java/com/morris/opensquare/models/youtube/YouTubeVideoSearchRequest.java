package com.morris.opensquare.models.youtube;

import com.morris.opensquare.models.kafka.OpenSquareTaskRequest;

/**
 * The {@link YouTubeVideoSearchRequest} class is a scalable class used to conduct YouTube Video searches
 * in opensquare /youtube/video_search api. As the opensquare youtube platform grows and more
 * request parameters are added to the api service, the request parameters can be added here.
 */
public class YouTubeVideoSearchRequest extends OpenSquareTaskRequest {
    private String videoId;

    public YouTubeVideoSearchRequest(String videoId, String name) {
        super(name);
        this.videoId = videoId;
    }

    public YouTubeVideoSearchRequest() {}

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
