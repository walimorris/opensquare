package com.morris.opensquare.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.search.FieldSearchPath;
import com.mongodb.client.model.search.SearchPath;
import com.morris.opensquare.models.youtube.YouTubeComment;
import com.morris.opensquare.models.youtube.YouTubeTranscribeSegment;
import com.morris.opensquare.models.youtube.YouTubeVideo;
import com.morris.opensquare.repositories.YouTubeVideoRepository;
import com.morris.opensquare.services.YouTubeService;
import com.morris.opensquare.services.loggers.LoggerService;
import com.morris.opensquare.utils.Constants;
import com.morris.opensquare.utils.ExternalServiceUtil;
import com.morris.opensquare.utils.JsonValidationUtil;
import com.morris.opensquare.utils.PythonScriptEngine;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.vectorSearch;
import static com.mongodb.client.model.Projections.*;
import static com.morris.opensquare.utils.Constants.REGEX_EMPTY;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Service
public class YouTubeServiceImpl implements YouTubeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeServiceImpl.class);

    private static final String URL = "url";
    private static final String SNIPPET = "snippet";
    private static final String SNIPPET_REPLIES = "snippet,replies";
    private static final String SNIPPER_CONTENT_DETAILS_STATISTICS = "snippet,contentDetails,statistics";
    private static final String YOUTUBE_URL_WITH_VIDEO_PARAM = "https://www.youtube.com/watch?v=";
    private static final String YOUTUBE_VECTOR_SEARCH_INDEX = "youtube_vector_search";
    private static final String YOUTUBE_VECTOR_SEARCH_PATH = "transcriptEmbeddings";
    private static final String YOUTUBE_VIDEOS_COLLECTION = "youtube_videos";
    private static final String YOUTUBE_VIDEO_JSON_VALIDATION_SCHEMA = "backend/src/main/resources/schemas/YouTubeVideo.json";
    private static final String SCORE = "score";
    private static final String _ID = "_id";
    private static final String VIDEO_URL = "videoUrl";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String PUBLISH_DATE = "publishDate";
    private static final String VIEW_COUNT = "viewCount";
    private static final String LIKE_COUNT = "likeCount";
    private static final String LENGTH = "length";
    private static final String THUMB_NAIL = "thumbnail";
    private static final String TRANSCRIPT = "transcript";
    private static final String DESCRIPTION = "description";
    private static final String CHANNEL_ID = "channelId";
    private static final String VIDEO_ID = "videoId";
    private static final String TRANSCRIPT_SEGMENTS = "transcriptSegments";
    private static final String TRANSCRIPT_EMBEDDINGS = "transcriptEmbeddings";
    private final ExternalServiceUtil externalServiceUtil;
    private final JsonValidationUtil jsonValidationUtil;
    private final LoggerService loggerService;
    private final PythonScriptEngine pythonScriptEngine;
    private final YouTubeVideoRepository youTubeVideoRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public YouTubeServiceImpl(ExternalServiceUtil externalServiceUtil, LoggerService loggerService,
                              PythonScriptEngine pythonScriptEngine, YouTubeVideoRepository youTubeVideoRepository,
                              MongoTemplate mongoTemplate, JsonValidationUtil jsonValidationUtil) {

        this.externalServiceUtil = externalServiceUtil;
        this.jsonValidationUtil = jsonValidationUtil;
        this.loggerService = loggerService;
        this.pythonScriptEngine = pythonScriptEngine;
        this.youTubeVideoRepository = youTubeVideoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public PageInfo setPageInfo(int resultsPerPage, int totalResults) {
        return new PageInfo()
                .setResultsPerPage(resultsPerPage)
                .setTotalResults(totalResults);
    }

    @Override
    public YouTubeComment marshallYoutubeComment(JSONObject object) {
        YouTubeComment youtubeComment = null;
        try {
            youtubeComment = new ObjectMapper().readValue(object.toString(), YouTubeComment.class);
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

    private Video videoFromVideoId(String videoId, String key, String parts) {
        Video video = null;
        try {
            YouTube youTubeService = externalServiceUtil.getYouTubeService("opensentop");
            YouTube.Videos.List request = youTubeService.videos()
                    .list(parts);
            VideoListResponse response = request.setKey(key)
                    .setId(videoId)
                    .execute();
            video = response.getItems().get(0);
        } catch (IOException e) {
            loggerService.saveLog(e.getClass().getName(), "Error parsing YouTube video from given videoId of " + videoId + e.getMessage(), Optional.of(LOGGER));
        }
        return video;
    }

    @Override
    public YouTubeVideo youTubeVideoTranscribeItem(String videoId, String googleKey, String openaiKey, List<YouTubeTranscribeSegment> transcriptSegments) {
        Video video = videoFromVideoId(videoId, googleKey, SNIPPER_CONTENT_DETAILS_STATISTICS);
        VideoSnippet snippet = video.getSnippet();
        VideoContentDetails contentDetails = video.getContentDetails();
        VideoStatistics statistics = video.getStatistics();
        String transcript = getContinuousTranscriptFromYouTubeTranscribeSegments(transcriptSegments);
        List<Double> embeddings = getTextEmbeddingsAda002(openaiKey, transcript);

        // handle null stat values
        long viewCount = statistics.getViewCount() == null ? 0 : statistics.getViewCount().longValue();
        long likeCount = statistics.getLikeCount() == null ? 0 : statistics.getLikeCount().longValue();

        // build item
        YouTubeVideo videoResult = new YouTubeVideo.Builder()
                .id(getObjectId())
                .videoUrl(YOUTUBE_URL_WITH_VIDEO_PARAM + videoId)
                .title(snippet.getTitle())
                .author(snippet.getChannelTitle())
                .publishDate(LocalDateTime.ofInstant(Instant.parse(snippet.getPublishedAt().toString()), ZoneId.systemDefault()))
                .viewCount(viewCount)
                .likeCount(likeCount)
                .length(contentDetails.getDuration())
                .thumbnail(snippet.getThumbnails().getDefault().getUrl())
                .transcript(transcript)
                .description(snippet.getDescription())
                .channelId(snippet.getChannelId())
                .videoId(videoId)
                .transcriptSegments(transcriptSegments)
                .transcriptEmbeddings(embeddings)
                .build();

        if (jsonValidationUtil.isValidJsonSchema(YOUTUBE_VIDEO_JSON_VALIDATION_SCHEMA, videoResult, YouTubeVideo.class)) {
            return videoResult;
        }
        return null;
    }

    @Override
    public YouTubeVideo insertYouTubeVideo(YouTubeVideo video) {
        if (video != null) {
            return youTubeVideoRepository.insert(video);
        }
        return null;
    }

    @Override
    public YouTubeVideo findOneYouTubeVideoByVideoId(String videoId) {
        return youTubeVideoRepository.findOneByVideoId(videoId);
    }

    @Override
    public String getContinuousTranscriptFromYouTubeTranscribeSegments(List<YouTubeTranscribeSegment> segments) {
        StringBuilder continuousTranscriptBuilder = new StringBuilder();
        for (YouTubeTranscribeSegment segment: segments) {
            String segmentText = segment.getText();
            continuousTranscriptBuilder.append(segmentText).append(REGEX_EMPTY);
        }
        return continuousTranscriptBuilder.toString();
    }

    @Override
    public String channelIdFromVideoId(String videoId, String key) {
        Video video = videoFromVideoId(videoId, key, SNIPPET);
        VideoSnippet videoSnippet = video.getSnippet();
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

    @Override
    public List<YouTubeVideo> getYouTubeVideosFromVectorSearch(@NonNull String key, @NonNull String searchQuery) {
        List<YouTubeVideo> videoResults = new ArrayList<>();
        if (!searchQuery.isEmpty()) {
            List<Double> searchQueryEmbeddings = getTextEmbeddingsAda002(key, searchQuery);
            if (!searchQueryEmbeddings.isEmpty()) {

                // TODO: refactor registry to config
                CodecRegistry myRegistry = fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(
                                PojoCodecProvider.builder()
                                        .register(YouTubeVideo.class, YouTubeTranscribeSegment.class)
                                        .build()
                        )
                );
                MongoDatabase db = mongoTemplate.getDb().withCodecRegistry(myRegistry);
                MongoCollection<YouTubeVideo> collection = db.getCollection(YOUTUBE_VIDEOS_COLLECTION, YouTubeVideo.class);
                FieldSearchPath fieldSearchPath = SearchPath.fieldPath(YOUTUBE_VECTOR_SEARCH_PATH);
                int candidates = 200, limit = 10;

                // TODO: add criteria on UI - such as gte or lte publishDate field and pull highest scored results
                List<Bson> pipeline = asList(
                        vectorSearch(
                                fieldSearchPath,
                                searchQueryEmbeddings,
                                YOUTUBE_VECTOR_SEARCH_INDEX,
                                candidates,
                                limit),

                        project(
                                fields(metaVectorSearchScore(SCORE),
                                        include(_ID),
                                        include(VIDEO_URL),
                                        include(TITLE),
                                        include(AUTHOR),
                                        include(PUBLISH_DATE),
                                        include(VIEW_COUNT),
                                        include(LIKE_COUNT),
                                        include(LENGTH),
                                        include(THUMB_NAIL),
                                        include(TRANSCRIPT),
                                        include(DESCRIPTION),
                                        include(CHANNEL_ID),
                                        include(VIDEO_ID),
                                        include(TRANSCRIPT_SEGMENTS),
                                        include(TRANSCRIPT_EMBEDDINGS))));

                // run query and marshall results
                collection.aggregate(pipeline).forEach(video -> {
                    LOGGER.info("{}", video.toString());
                    videoResults.add(video);
                });
            }
        }
        return videoResults;
    }

    @Override
    public List<YouTubeTranscribeSegment> getYouTubeTranscribeSegmentsFromVideoId(String videoId) {
        String videoUrl = YOUTUBE_URL_WITH_VIDEO_PARAM + videoId;
        return pythonScriptEngine.processPythonTranscribeScript(videoUrl);
    }

    @Override
    public List<Double> getTextEmbeddingsAda002(String key, String text) {
        return pythonScriptEngine.processPythonOpenAiAda002TextEmbeddingScript(key, text);
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

    private ObjectId getObjectId() {
        Date date = new Date();
        return new ObjectId(date);
    }
}
