package com.morris.opensquare.services.kafka;

import com.morris.opensquare.models.kafka.OpenSquareTaskStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;

public interface OpenSquareKafkaProducerService {

    /**
     * (Producer) Sends message to Kafka broker.
     *
     * @param topicName {@link String} name of event topic
     * @param key {@link String} key value for the event topic
     * @param value {@link OpenSquareTaskStatus} object which holds properties, including the event value
     *
     * @see KafkaTemplate
     */
    void send(@NonNull String topicName, @NonNull String key, @NonNull OpenSquareTaskStatus value);
}
