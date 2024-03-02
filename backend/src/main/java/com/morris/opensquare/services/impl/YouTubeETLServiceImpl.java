package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.Status;
import com.morris.opensquare.models.kafka.OpenSquareTaskStatus;
import com.morris.opensquare.models.youtube.YouTubeVideoSearchRequest;
import com.morris.opensquare.services.*;
import com.morris.opensquare.services.kafka.OpenSquareKafkaConsumerService;
import com.morris.opensquare.services.kafka.OpenSquareKafkaProducerService;
import com.morris.opensquare.services.trackers.PlatformAnalysisTrackerService;
import com.morris.opensquare.utils.ApplicationConfigurationUtil;
import com.morris.opensquare.utils.ExternalServiceUtil;
import jakarta.servlet.http.HttpSession;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.morris.opensquare.utils.Constants.ERROR_YOUTUBE_TASK_INTERRUPTED;
import static com.morris.opensquare.utils.Constants.LOWERCASE_YOUTUBE;

@Service
public class YouTubeETLServiceImpl implements YouTubeETLService {
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeETLServiceImpl.class);

    @Autowired KafkaConsumer<String, OpenSquareTaskStatus> kafkaConsumer;
    @Autowired OpenSquareKafkaProducerService kafkaProducerService;
    @Autowired KafkaAdmin kafkaAdmin;
    @Autowired YouTubeService youTubeService;
    @Autowired TextAnalyzerService textAnalyzerService;
    @Autowired FileService fileService;
    @Autowired ExternalServiceUtil externalServiceUtil;
    @Autowired ApplicationConfigurationUtil applicationConfigurationUtil;
    @Autowired SecretsService secretsService;
    @Autowired ObjectMapperService objectMapperService;
    @Autowired OpenSquareKafkaConsumerService consumerService;
    @Autowired PlatformAnalysisTrackerService platformAnalysisTracker;

    private static final String INDEX_FILE_JSON_FILE_PATH = "backend/src/main/resources/etl/indexzone.json";
    private static final String CSV_FILE_NAME = "backend/src/main/resources/etl/comments.csv";
    private static final String CLEAN_COMMENTS = "backend/src/main/resources/etl/cleaned_comments.csv";
    private static final String TASKS_URL_FRAGMENT = "/tasks/";
    private static final String PROGRESS_URL_FRAGMENT = "/progress";
    private static final String PLATFORM = LOWERCASE_YOUTUBE;

    @Async
    @Override
    public void process(@NonNull String taskId,
                        @NonNull YouTubeVideoSearchRequest videoSearch,
                        @NonNull UriComponentsBuilder componentsBuilder,
                        @NonNull HttpSession session) {
        try {
            createNewTopic(taskId);
            UriComponents resultURL = componentsBuilder.path(TASKS_URL_FRAGMENT + taskId + PROGRESS_URL_FRAGMENT).build();
            updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 0.0f, Status.SUBMITTED, null));
            Thread.sleep(2000L);
            updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 5.0f, Status.STARTED, null));
            if (textAnalyzerService.isValidYoutubeVideoId(videoSearch.getVideoId())) {
                updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 16.0f, Status.PREP_FILES_CSV, null));

                executeYouTubeExtractionProcess(videoSearch.getVideoId());
                updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 33.0f, Status.EXTRACTING_DATA_SET, null));
            }
            updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 100.0f, Status.FINISHED, resultURL.toUriString()));

        } catch (InterruptedException | ExecutionException e) {
            updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 100.0f, Status.TERMINATED, null));
            LOGGER.error(ERROR_YOUTUBE_TASK_INTERRUPTED, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void executeYouTubeExtractionProcess(String videoId) {}

    private void createNewTopic(String topicName) throws ExecutionException, InterruptedException {
        Map<String, String> topicConfiguration = new HashMap<>();
        // 24 hours retention
        topicConfiguration.put(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(24 * 60 * 60 * 1000));

        NewTopic newTopic = new NewTopic(topicName, 1, (short) 1).configs(topicConfiguration);
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        }
        kafkaConsumer.subscribe(Collections.singletonList(topicName));
    }

    private void updateTaskExecutionProgress(OpenSquareTaskStatus taskStatus) {
        kafkaProducerService.send(taskStatus.getTaskId(), taskStatus.getTaskId(), taskStatus);
    }
}
