package com.morris.opensquare.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.morris.opensquare.models.youtube.YoutubeComment;
import com.morris.opensquare.services.YouTubeService;
import com.morris.opensquare.services.loggers.LoggerService;
import com.morris.opensquare.utils.Constants;
import com.morris.opensquare.utils.ExternalServiceUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class YouTubeServiceImpl implements YouTubeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeServiceImpl.class);

    private static final String SNIPPET = "snippet";
    private static final String SNIPPET_REPLIES = "snippet,replies";

    private final ExternalServiceUtil externalServiceUtil;
    private final LoggerService loggerService;

    @Autowired
    public YouTubeServiceImpl(ExternalServiceUtil externalServiceUtil, LoggerService loggerService) {
        this.externalServiceUtil = externalServiceUtil;
        this.loggerService = loggerService;
    }

    @Override
    public PageInfo setPageInfo(int resultsPerPage, int totalResults) {
        return new PageInfo()
                .setResultsPerPage(resultsPerPage)
                .setTotalResults(totalResults);
    }

    @Override
    public YoutubeComment marshallYoutubeComment(JSONObject object) {
        YoutubeComment youtubeComment = null;
        try {
            youtubeComment = new ObjectMapper().readValue(object.toString(), YoutubeComment.class);
        } catch (JsonProcessingException e) {
            loggerService.saveLog(e.getClass().getName(), "Error marshalling json object referencing YouTube comment to it's model class: " + e.getMessage(), Optional.of(LOGGER));
        }
        return youtubeComment;
    }

    @Override
    public String removeAuthorChannelDecorFromYoutubeAPIComment(String value) {
        return value.split("=")[1]
                .trim()
                .split("}")[0]
                .trim();
    }

    @Override
    public String channelIdFromVideoId(String videoId, String key) {
        VideoSnippet videoSnippet = null;
        try {
            YouTube youTubeService = externalServiceUtil.getYouTubeService("opensentop");
            YouTube.Videos.List request = youTubeService.videos()
                    .list(SNIPPET);
            VideoListResponse response = request.setKey(key)
                    .setId(videoId)
                    .execute();
            videoSnippet = response.getItems().get(0).getSnippet();
        } catch (IOException e) {
            loggerService.saveLog(e.getClass().getName(), "Error parsing YouTube channel id from given videoId of " + videoId + e.getMessage(), Optional.of(LOGGER));
        }
        if (videoSnippet != null) {
            return videoSnippet.getChannelId();
        }
        return null;
    }

    public Channel channelFromUserName(String userName, String key) throws IOException {
        YouTube youTubeService = externalServiceUtil.getYouTubeService("opensentop");
        YouTube.Channels.List request = youTubeService.channels()
                .list("snippet,contentDetails,topicDetails");
        ChannelListResponse response = request.setKey(key)
                .setForUsername(userName)
                .execute();
        return response.getItems().get(0);
    }

    public List<String> getChannelTopics(String userName, String key) throws IOException {
        // returns links to topic wikipedia pages
        List<String> rawTopics = getChannelTopicsRaw(userName, key);
        List<String> strippedTopics = new ArrayList<>();

        rawTopics.forEach(topic -> {
            String[] splitTopic = topic.split("/");
            strippedTopics.add(splitTopic[splitTopic.length - 1]);
        });
        return strippedTopics;
    }

    public List<String> getChannelTopicsRaw(String userName, String key) throws IOException {
        Channel channel = channelFromUserName(userName, key);
        return channel.getTopicDetails().getTopicCategories();
    }

    @Override
    public Map<String, String> analyzeYoutubeChannelMetaData(String channelId, String key) throws IOException {
        YouTube youTubeService = externalServiceUtil.getYouTubeService("opensentop");
        YouTube.Channels.List request = youTubeService.channels().list(SNIPPET);

        ChannelListResponse response = request.setKey(key)
                .setId(channelId)
                .execute();

        ChannelSnippet channelSnippet = response.getItems().get(0).getSnippet();

        HashMap<String, String> meta = new HashMap<>();
        meta.put(Constants.YOUTUBE_PROPERTY_CHANNEL_ID, channelId);
        meta.put(Constants.YOUTUBE_PROPERTY_CHANNEL_TITLE, channelSnippet.getTitle());
        meta.put(Constants.YOUTUBE_PROPERTY_CHANNEL_DESCRIPTION, channelSnippet.getDescription());
        meta.put(Constants.YOUTUBE_PROPERTY_COUNTRY, channelSnippet.getCountry());
        return meta;
    }

    @Override
    public List<CommentThread> getCommentItems(String applicationName, String key, String videoId) throws IOException {
        YouTube youtubeservice = externalServiceUtil.getYouTubeService(applicationName);
        YouTube.CommentThreads.List request = youtubeservice.commentThreads()
                .list(SNIPPET_REPLIES);

        PageInfo pageInfo = setPageInfo(40, 40);

        CommentThreadListResponse response = request.setKey(key)
                .setVideoId(videoId)
                .execute()
                .setPageInfo(pageInfo);

        return response.getItems();
    }

    @Override
    public String[] getYouTubeCommentCSVHeaders() {
        return new String[]{
                Constants.YOUTUBE_PROPERTY_CHANNEL_DESCRIPTION,
                Constants.YOUTUBE_PROPERTY_COUNTRY,
                Constants.YOUTUBE_PROPERTY_COMMENT_ID,
                Constants.YOUTUBE_PROPERTY_AUTHOR_CHANNEL_ID,
                Constants.YOUTUBE_PROPERTY_VIDEO_ID,
                Constants.YOUTUBE_PROPERTY_COMMENT,
                Constants.YOUTUBE_PROPERTY_LIKE_COUNT,
                Constants.YOUTUBE_PROPERTY_CHANNEL_ID,
                Constants.YOUTUBE_PROPERTY_PARENT_ID,
                Constants.YOUTUBE_PROPERTY_CHANNEL_TITLE
        };
    }
}
