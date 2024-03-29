package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.Status;
import com.morris.opensquare.models.kafka.OpenSquareTaskStatus;
import com.morris.opensquare.models.youtube.YouTubeVideoSearchRequest;
import com.morris.opensquare.services.TextAnalyzerService;
import com.morris.opensquare.services.YouTubeTaskService;
import com.morris.opensquare.services.kafka.OpenSquareKafkaProducerService;
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

@Service
public class YouTubeTaskServiceImpl implements YouTubeTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeTaskServiceImpl.class);

    private final KafkaConsumer<String, OpenSquareTaskStatus> kafkaConsumer;
    private final OpenSquareKafkaProducerService kafkaProducerService;
    private final KafkaAdmin kafkaAdmin;
    private final TextAnalyzerService textAnalyzerService;

    private static final String TASKS_URL_FRAGMENT = "/tasks/";
    private static final String PROGRESS_URL_FRAGMENT = "/progress";

    @Autowired
    public YouTubeTaskServiceImpl(KafkaConsumer<String, OpenSquareTaskStatus> kafkaConsumer, OpenSquareKafkaProducerService kafkaProducerService,
                                  KafkaAdmin kafkaAdmin, TextAnalyzerService textAnalyzerService) {

        this.kafkaConsumer = kafkaConsumer;
        this.kafkaProducerService = kafkaProducerService;
        this.kafkaAdmin = kafkaAdmin;
        this.textAnalyzerService = textAnalyzerService;

    }

    @Async
    @Override
    public void process(@NonNull String taskId,
                        @NonNull YouTubeVideoSearchRequest videoSearch,
                        @NonNull UriComponentsBuilder componentsBuilder) {
        try {
            createNewTopic(taskId);
            UriComponents resultURL = componentsBuilder.path(TASKS_URL_FRAGMENT + taskId + PROGRESS_URL_FRAGMENT).build();
            updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 0.0f, Status.SUBMITTED, null));
            Thread.sleep(2000L);
            updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 5.0f, Status.STARTED, null));
            if (textAnalyzerService.isValidYoutubeVideoId(videoSearch.getVideoId())) {
                updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 16.0f, Status.PREP_FILES_CSV, null));
                updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 33.0f, Status.EXTRACTING_DATA_SET, null));
            }
            updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 100.0f, Status.FINISHED, resultURL.toUriString()));

        } catch (InterruptedException | ExecutionException e) {
            updateTaskExecutionProgress(new OpenSquareTaskStatus(taskId, videoSearch.getName(), 100.0f, Status.TERMINATED, null));
            LOGGER.error(ERROR_YOUTUBE_TASK_INTERRUPTED, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

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
