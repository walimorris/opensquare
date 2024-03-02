package com.morris.opensquare.services.kafka.impl;

import com.morris.opensquare.models.kafka.OpenSquareTaskStatus;
import com.morris.opensquare.services.kafka.OpenSquareKafkaConsumerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.morris.opensquare.utils.Constants.LATEST_STATUS;

@Service
public class OpenSquareKafkaConsumerServiceImpl implements OpenSquareKafkaConsumerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenSquareKafkaConsumerServiceImpl.class);

    private final KafkaConsumer<String, OpenSquareTaskStatus> kafkaConsumer;

    @Autowired
    OpenSquareKafkaConsumerServiceImpl(KafkaConsumer<String, OpenSquareTaskStatus> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    @Override
    public OpenSquareTaskStatus getLatestTaskStatus(@NonNull String taskId) {
        ConsumerRecord<String, OpenSquareTaskStatus> latestUpdate = null;
        ConsumerRecords<String, OpenSquareTaskStatus> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
        if (!consumerRecords.isEmpty()) {
            for (ConsumerRecord<String, OpenSquareTaskStatus> stringOpenSentTaskStatusConsumerRecord : consumerRecords.records(taskId)) {
                latestUpdate = stringOpenSentTaskStatusConsumerRecord;
            }
            if (latestUpdate != null) {
                LOGGER.info(LATEST_STATUS, latestUpdate.value());
            } else {
                LOGGER.info(LATEST_STATUS, "Null");
            }
        }
        return latestUpdate != null ? latestUpdate.value() : null;
    }
}
