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

    public Channel channelFromChannelId(String channelId, String key) {
        Channel channel = null;
        try {
            YouTube youTubeService = externalServiceUtil.getYouTubeService("opensentop");
            YouTube.Channels.List request = youTubeService.channels()
                    .list("snippet,contentDetails,statistics");
            ChannelListResponse response = request.setKey(key)
                    .setId(channelId)
                    .setMaxResults(1L)
                    .execute();
            channel = response.getItems().get(0);
        } catch (IOException e) {
            loggerService.saveLog(e.getClass().getName(), "Error getting Channel info with Id " + channelId + e.getMessage(), Optional.of(LOGGER));
        }
        return channel;
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

    @Override
    public Channel channelFromUserName(String userName, String key) {
        Channel channel = null;

        try {
            YouTube youTubeService = externalServiceUtil.getYouTubeService("opensentop");
            YouTube.Channels.List request = youTubeService.channels()
                    .list("snippet,contentDetails,topicDetails");
            ChannelListResponse response = request.setKey(key)
                    .setForUsername(userName)
                    .execute();
            channel = response.getItems().get(0);
        } catch (IOException e) {
            loggerService.saveLog(e.getClass().getName(), "Error query YouTube Channel from Username: " + userName + e.getMessage(), Optional.of(LOGGER));
        }
        return channel;
    }

    @Override
    public List<String> getChannelTopics(String userName, String key) {

        // returns links to topic wikipedia pages
        List<String> rawTopics = getChannelTopicsRaw(userName, key);
        List<String> strippedTopics = new ArrayList<>();

        rawTopics.forEach(topic -> {
            String[] splitTopic = topic.split("/");
            strippedTopics.add(splitTopic[splitTopic.length - 1]);
        });
        return strippedTopics;
    }
    @Override
    public List<String> getChannelTopicsRaw(String userName, String key) {
        Channel channel = channelFromUserName(userName, key);
        return channel.getTopicDetails().getTopicCategories();
    }

    @Override
    public List<Channel> getChannelsFromYouTubeByKeywordTopic(String keyword, String key) {
        SearchListResponse searchResponse = null;
        try {
            YouTube youTubeService = externalServiceUtil.getYouTubeService("opensentop");
            YouTube.Search.List request = youTubeService.search()
                    .list(SNIPPET);

            searchResponse = request.setKey(key)
                    .setMaxResults(10L)
                    .setQ(keyword)
                    .execute();
        } catch (IOException e) {
            loggerService.saveLog(e.getClass().getName(), "Error searching YouTube Topic Keyword: " + keyword + e.getMessage(), Optional.of(LOGGER));
        }
        if (searchResponse != null) {
            return channelsFromSearchResults(searchResponse.getItems(), key);
        }
        return null;
    }

    private List<Channel> channelsFromSearchResults(List<SearchResult> searchResults, String key) {
        List<Channel> channels = new ArrayList<>();
        for (SearchResult result : searchResults) {
            channels.add(channelFromChannelId(result.getSnippet().getChannelId(), key));
        }
        return channels;
    }

    @Override
    public List<CommentSnippet> getTopLevelCommentsFromUserOnYouTubeVideo(String user, String videoId, String key) {
        user = "@" + user; // youtube user-names start with @ symbol
        List<CommentSnippet> allTopLevelComments = getTopLevelCommentsFromYouTubeVideo("opensentop", key, videoId);
        List<CommentSnippet> topLevelCommentSnippetsFromUser = new ArrayList<>();

        for (CommentSnippet currentSnippet : allTopLevelComments) {
            if (user.equals(currentSnippet.getAuthorDisplayName())) {
                topLevelCommentSnippetsFromUser.add(currentSnippet);
            }
        }
        return topLevelCommentSnippetsFromUser;
    }

    @Override
    public List<CommentSnippet> getTopLevelCommentsFromYouTubeVideo(String applicationName, String key, String videoId) {
        YouTube youtubeservice = externalServiceUtil.getYouTubeService(applicationName);
        YouTube.CommentThreads.List request;
        PageInfo pageInfo = setPageInfo(20, 20);
        List<CommentSnippet> allTopLevelComments = new ArrayList<>();

        try {
            request = youtubeservice.commentThreads().list(SNIPPET_REPLIES);
            CommentThreadListResponse response =  request.setKey(key)
                    .setVideoId(videoId)
                    .execute()
                    .setPageInfo(pageInfo);

            List<CommentThread> comments = response.getItems();
            if (!comments.isEmpty()) {
                comments.parallelStream().forEach(commentThread -> {
                    call(commentThread, allTopLevelComments, Thread.currentThread().getName());
                });
                String nextPageToken = response.getNextPageToken();
                int loop = 1;

                while (nextPageToken != null) {
                    LOGGER.info("inside pagination loop: {}", loop);
                    response = request.setKey(key)
                            .setVideoId(videoId)
                            .setPageToken(nextPageToken)
                            .execute();
                    comments = response.getItems();
                    comments.parallelStream().forEach(commentThread -> {
                        call(commentThread, allTopLevelComments, Thread.currentThread().getName());
                    });

                    nextPageToken = response.getNextPageToken();
                    loop++;
                }
            }

        } catch (IOException e) {
            loggerService.saveLog(e.getClass().getName(), "Error searching YouTube video for user commments for video:  " + videoId + e.getMessage(), Optional.of(LOGGER));
        }

        return allTopLevelComments;
    }

    private void call(CommentThread commentThread, List<CommentSnippet> topLevelComments, String name) {
        LOGGER.info("adding snippet for thread {}: ", name);
        topLevelComments.add(commentThread.getSnippet().getTopLevelComment().getSnippet());
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
