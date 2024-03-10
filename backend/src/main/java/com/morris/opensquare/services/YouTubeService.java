package com.morris.opensquare.services;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.morris.opensquare.models.youtube.YouTubeVideo;
import com.morris.opensquare.utils.PythonScriptEngine;
import com.google.api.services.youtube.model.PageInfo;
import com.morris.opensquare.models.youtube.YouTubeTranscribeSegment;
import com.morris.opensquare.models.youtube.YouTubeComment;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

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
     * @return {@link YouTubeComment}
     */
    YouTubeComment marshallYoutubeComment(JSONObject object);

    /**
     * Removes the JSON-String decor from the author property from a YouTube comment.
     *
     * @param value {@link String}
     * @return {@link String}
     */
    String removeAuthorChannelDecorFromYoutubeAPIComment(String value);

    /**
     * Get YouTube Channel object from channelId.
     *
     * @param channelId {@link String} channelId
     * @param key {@link String} Google API Key
     *
     * @return {@link Channel}
     */
    Channel channelFromChannelId(String channelId, String key);

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
     * Gets YouTube {@link Channel} info based on the channel owners username.
     *
     * @param userName Owner - YouTube Channel username
     * @param key Google API key
     *
     * @return {@link Channel}
     */
    Channel channelFromUserName(String userName, String key);

    /**
     * Get YouTube {@link Channel} topics. Channel Topics are links to the topics wikipedia page.
     * Functionality for this method strips the wikipedia link and pulls the respective topics.
     * An example of a topic is: society or comedy
     *
     * @param userName Owner - YouTube channel username
     * @param key Google API key
     *
     * @return {@link List<String>}
     */
    List<String> getChannelTopics(String userName, String key);

    /**
     * Get YouTube {@link Channel} topics in raw format, i.e. the full wikipedia url link to the topic decription.
     *
     * @param userName Owner - YouTube Channel Username
     * @param key Google API key
     *
     * @return {@link List<String>}
     */
    List<String> getChannelTopicsRaw(String userName, String key);

    /**
     * Get Channel results based on YouTube keyword search.
     *
     * @param keyword {@link String} search query
     * @param key {@link String} Google API key
     *
     * @return {@link List<Channel>}
     */
    List<Channel> getChannelsFromYouTubeByKeywordTopic(String keyword, String key);

    /**
     * Get TopLevel Comments from a specific user on a YouTube video, given the video id. It should be noted that
     * these results are limited to top level comments. Nested comments will be released in future.
     *
     * @param user {@link String} youtube user name
     * @param videoId {@link String} youtube videoId
     * @param key {@link String} youtube api key
     *
     * @see <a href="https://developers.google.com/youtube/v3/docs/commentThreads/list">YouTube Docs</a>
     *
     * @return {@link List<CommentSnippet>}
     */
    List<CommentSnippet> getTopLevelCommentsFromUserOnYouTubeVideo(String user, String videoId, String key);

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
     * Gets TopLevel Comments from the {@link YouTube} google service on a specific video.
     *
     * @param applicationName YouTube's application name for service
     * @param key youtube api key
     * @param videoId videoId for youtube video
     *
     * @see <a href="https://developers.google.com/youtube/v3/docs/commentThreads/list">YouTube Docs</a>
     *
     * @return {@link CommentThreadListResponse}
     */
    List<CommentSnippet> getTopLevelCommentsFromYouTubeVideo(String applicationName, String key, String videoId);

    /**
     * Utilizes Opensquare's {@link PythonScriptEngine} to transcribe YouTube video and return
     * a {@link List} of transcribed segments depicting the timestamp and text.
     *
     * @param videoId {@link String} YouTuve videoId
     *
     * @return {@link List<YouTubeTranscribeSegment>}
     */
    List<YouTubeTranscribeSegment> getYouTubeTranscribeSegmentsFromVideoId(String videoId);

    /**
     * Creates a {@link YouTubeVideo} object from given youtube videoId and transcribe segments.
     *
     * @param videoId {@link String} YouTube videoId
     * @param googleKey {@link String} YouTube API key
     * @param openaiKey {@link String} OpenAI API key
     * @param transcriptSegments {@link List<YouTubeTranscribeSegment>}
     *
     * @return {@link YouTubeVideo}
     */
    YouTubeVideo youTubeVideoTranscribeItem(String videoId, String googleKey, String openaiKey, List<YouTubeTranscribeSegment> transcriptSegments);

    /**
     * Get a continuous string with no formatting from a List of {@link YouTubeTranscribeSegment}. The only
     * change would be proper spacing, with no line breaks.
     *
     * @param segments {@link List<YouTubeTranscribeSegment>}
     *
     * @return {@link String}
     */
    String getContinuousTranscriptFromYouTubeTranscribeSegments(List<YouTubeTranscribeSegment> segments);

    /**
     * Gets embeddings from openai text-embedding-ada-002 model.
     *
     * @param key {@link String} openai key
     * @param text {@link String} text to embed
     *
     * @return {@link List<Double>}
     */
    List<Double> getTextEmbeddingsAda002(String key, String text);

    /**
     * Get YouTube videos from vector index search on stored {@link YouTubeVideo}.
     *
     * @param key {@link String} openai api key
     * @param searchQuery {@link String} search query
     *
     * @return {@link List<YouTubeVideo>}
     */
    List<YouTubeVideo> getYouTubeVideosFromVectorSearch(@NonNull String key, @NonNull String searchQuery);

    /**
     * Used within CSVHeaders to unwrap and marshall {@link YouTubeComment} in CSV files.
     * NOTE: The CSVHeader properties should read in the same order as they appear in the
     * actual CSV file.
     *
     * @see CSVRecord
     *
     * @return String[]
     */
    String[] getYouTubeCommentCSVHeaders();

    /**
     * Insert {@link YouTubeVideo} to MongoDB youtube_videos collection.
     *
     * @param video {@link YouTubeVideo}
     *
     * @return {@link YouTubeVideo}
     */
    YouTubeVideo insertYouTubeVideo(YouTubeVideo video);

    /**
     * Find one {@link YouTubeVideo} by videoId.
     *
     * @param videoId {@link String} YouTube videoId
     *
     * @return {@link YouTubeVideo}
     */
    YouTubeVideo findOneYouTubeVideoByVideoId(String videoId);
}
