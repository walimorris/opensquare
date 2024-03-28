package com.morris.opensquare.services;

import com.morris.opensquare.models.kafka.OpenSquareTaskStatus;
import com.morris.opensquare.models.youtube.YouTubeVideoSearchRequest;
import com.morris.opensquare.services.kafka.OpenSquareKafkaProducerService;
import org.springframework.lang.NonNull;
import org.springframework.web.util.UriComponentsBuilder;

public interface YouTubeTaskService {

    /**
     * Processes a new YouTube video search ETL transaction. Utilizes the {@link OpenSquareKafkaProducerService}
     * to send new {@link OpenSquareTaskStatus} tasks to Kafka cluster.
     *
     * @param taskId {@link String} taskId for event
     * @param videoSearch {@link YouTubeVideoSearchRequest}
     * @param componentsBuilder {@link UriComponentsBuilder}
     *
     * @see OpenSquareKafkaProducerService#send(String, String, OpenSquareTaskStatus)
     */
    void process(@NonNull String taskId, @NonNull YouTubeVideoSearchRequest videoSearch,
            @NonNull UriComponentsBuilder componentsBuilder);
}
