package com.morris.opensquare.services;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.PageInfo;
import com.morris.opensquare.models.youtube.YoutubeComment;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface YouTubeService {

    /**
     * Allows caller to set PageInfo settings for results.
     *
     * @param resultsPerPage set number of results per page
     * @param totalResults total number of page results
     *
     * @return {@link PageInfo}
     */
    PageInfo setPageInfo(int resultsPerPage, int totalResults);

    /**
     * Converts a YouTube comment, in JSON form, to its model class.
     *
     * @param object {@link JSONObject}
     *
     * @return {@link YoutubeComment}
     */
    YoutubeComment marshallYoutubeComment(JSONObject object);

    /**
     * Removes the JSON-String decor from the author property from a YouTube comment.
     *
     * @param value {@link String}
     * @return {@link String}
     */
    String removeAuthorChannelDecorFromYoutubeAPIComment(String value);

    /**
     * Get a YouTube channel id from a given YouTube video id.
     *
     * @param videoId {@link String} YouTube videoId
     * @param key {@link String} YouTube service key
     *
     * @return {@link String} the video's channel id
     */
    String channelIdFromVideoId(String videoId, String key);

    /**
     * Constructs a mapping structure of key metadata from a YouTube video's channel id.
     *
     * @param channelId {@link String} YouTube video id
     * @param key {@link String} YouTube service key
     *
     * @return {@link HashMap}
     *
     * @throws IOException
     */
    Map<String, String> analyzeYoutubeChannelMetaData(String channelId, String key) throws IOException;

    /**
     * Gets CommentItems from the {@link YouTube} google service.
     *
     * @param applicationName YouTube's application name for service
     * @param key youtube api key
     * @param videoId videoId for youtube video
     *
     * @see <a href="https://developers.google.com/youtube/v3/docs/commentThreads/list">YouTube Docs</a>
     *
     * @return {@link List<CommentThread>}
     */
    List<CommentThread> getCommentItems(String applicationName, String key, String videoId) throws IOException;

    /**
     * Used within CSVHeaders to unwrap and marshall {@link YoutubeComment} in CSV files.
     * NOTE: The CSVHeader properties should read in the same order as they appear in the
     * actual CSV file.
     *
     * @see CSVRecord
     *
     * @return String[]
     */
    String[] getYouTubeCommentCSVHeaders();
}
