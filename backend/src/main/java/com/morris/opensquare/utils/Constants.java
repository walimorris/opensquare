package com.morris.opensquare.utils;

public final class Constants {

    /**
     * The caller references the constants using <tt>Constants.SOME_STRING</tt>.
     * Thus, the caller should be prevented from constructing objects of this
     * class, by declaring this private constructor.
     */
    private Constants() {
        throw new AssertionError();
    }

    public static final String YOUTUBE_PROPERTY_CHANNEL_ID = "channelId";
    public static final String YOUTUBE_PROPERTY_CHANNEL_TITLE = "channelTitle";
    public static final String YOUTUBE_PROPERTY_CHANNEL_DESCRIPTION = "channelDescription";
    public static final String YOUTUBE_PROPERTY_COUNTRY = "country";
    public static final String YOUTUBE_PROPERTY_SNIPPET = "snippet";
    public static final String YOUTUBE_PROPERTY_SENTIMENT_SCORE = "sentimentScore";
    public static final String YOUTUBE_PROPERTY_COMMENT_ID = "commentId";
    public static final String YOUTUBE_PROPERTY_PARENT_ID = "parentId";
    public static final String YOUTUBE_PROPERTY_AUTHOR_CHANNEL_ID = "authorChannelId";
    public static final String YOUTUBE_PROPERTY_VIDEO_ID = "videoId";
    public static final String YOUTUBE_PROPERTY_COMMENT = "comment";
    public static final String YOUTUBE_PROPERTY_LIKE_COUNT = "likeCount";
    public static final String SIGNING_SERVICE_NAME_ES = "es";
    public static final String LOWERCASE_YOUTUBE = "youtube";
    public static final String LOWERCASE_OPENSENT = "opensent";
    public static final String COMMENTS = "comments";
    public static final String CSV = ".csv";
    public static final String S3_PREFIX = "s3://";
    public static final String DB = "db";
    public static final String CRAWLER = "crawler";
    public static final String DATASET = "dataset";
    public static final String JOB = "job";
    public static final String PARENT_ID = "parentId";
    public static final String POSITIVE = "POSITIVE";
    public static final String NEGATIVE = "NEGATIVE";
    public static final String NEUTRAL = "NEUTRAL";
    public static final String STATE_RUNNING = "RUNNING";
    public static final String STATE_STOPPING = "STOPPING";
    public static final String STATE_READY = "READY";
    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";
    public static final String REASON = "reason";
    public static final String UNSUCCESSFUL = "unsuccessful";
    public static final String LOGIN_UNSUCCESSFUL = "login unsuccessful";

    // Whois constants
    public static final String WHO_IS_COMMAND = "whois ";
    public static final String WHO_IS_THIN_COMMAND = "whois -h whois.verisign-grs.com domain ";
    public static final String NAME_SERVER = "nameServer";
    public static final String MAC_OS_X = "Mac OS X";
    public static final String LINUX = "Linux";
    public static final String WINDOWS = "Windows";

    public static final String REDACTED = "REDACTED FOR PRIVACY";

    // NSLookup constants
    public static final String NSLOOKUP_COMMAND = "nslookup ";

    // REGEX constants
    public static final String REGEX_EMPTY = " ";
    public static final String REGEX_COLON = ":";
    public static final String REGEX_HYPHEN = "-";
    public static final String REGEX_FORWARD_SLASH = "/";
    public static final String REGEX_COLON_WITH_SPACE = ": ";
    public static final String REGEX_STRICT_STRING_NUMBERS = "^[A-Za-z0-9]*$";
    public static final String REGISTRARS = "Registrars.";

    // Controller
    public static final String INDEX = "index";

    // Service
    public static final String LATEST_STATUS = "Latest updated status : {}";
    public static final String TASK_STATUS_SEND_KAFKA = "Task status sent to Kafka topic: {}";
    public static final String CASSANDRA_TIMESTAMP_FORMAT = "yyyy-MM-dd";
    public static final String PST = "America/Los_Angeles";
    public static final String TEST_ENGINE = "junit";

    // Errors - LOGS
    public static final String ERROR_GOOGLE_CUSTOM_SEARCH_SERVICE = "Error getting Google CustomSearch service: {}";
    public static final String ERROR_YOUTUBE_SERVICE_1 = "YouTube Service unable to build and connect, please check httpTransport connection";
    public static final String ERROR_YOUTUBE_SERVICE_2 = "Error establishing Youtube Service HTTP connection to application '{}': {}";
    public static final String VIDEO_IN_INDEX = "video present in index = {}";
    public static final String ERROR_YOUTUBE_TASK_INTERRUPTED = "Error YouTube Task process interrupted: {}";
    public static final String LOADING_DATA_INDEX_ZONE = "LOADING COLLECTED DATA TO INDEX ZONE";
    public static final String INSIDE_CRAWL_PROCESS = "INSIDE CRAWL PROCESS";
    public static final String DATA_BREW_JOB_CREATED = "DataBrew Job created: {}";
    public static final String DATA_BREW_JOB_RUNNING_WITH_ID = "Data Brew Job Running, with Id: {}";
    public static final String DATA_BREW_JOB_RUN_STATE = "Data Brew Job Current State: {}";
    public static final String ERROR_LOADING_YOUTUBE_COMMENTS_OPEN_SEARCH = "Error loading YouTube comments to OpenSearch index";
    public static final String CRAWLER_TABLE_CREATED = "crawler table created: {}";
    public static final String RESULT = "Result: {}";
    public static final String VIDEO_ID_SEARCH = "videoId Search: {}";
    public static final String ERROR_VALIDATING_URL = "Error validating url {}: {}";
    public static final String INSIDE_PAGINATION_LOOP = "Inside pagination loop: {}";
    public static final String VIDEO_PRESENT_IN_INDEX = "video: {} present in index: '{}'";

    // external urls
    public static final String OWASP_BLOG = "https://owasp.org/blog";
    public static final String OWASP_BLOG_MD_PREFIX = "md";
    public static final String OPENSQUARE_JAVA_MONGODB_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String OPENSQUARE_JAVA_MONGODB_JSON_PARSE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
}
