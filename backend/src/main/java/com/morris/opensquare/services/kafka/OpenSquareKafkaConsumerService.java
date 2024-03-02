package com.morris.opensquare.services.kafka;

import com.morris.opensquare.models.kafka.OpenSquareTaskStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.lang.NonNull;

import java.time.Duration;

public interface OpenSquareKafkaConsumerService {

    /**
     * (Consumer) Polls Kafka cluster, given the eventId (task) and returns status.
     *
     * @param taskId {@link String} the taskId of kafka topic
     *
     * @see KafkaConsumer#poll(Duration)
     * @see ConsumerRecord
     *
     * * @return {@link OpenSquareTaskStatus}
     */
    OpenSquareTaskStatus getLatestTaskStatus(@NonNull String taskId);
}
