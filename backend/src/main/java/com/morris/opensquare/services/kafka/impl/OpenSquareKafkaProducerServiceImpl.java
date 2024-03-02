package com.morris.opensquare.services.kafka.impl;

import com.morris.opensquare.models.kafka.OpenSquareTaskStatus;
import com.morris.opensquare.services.kafka.OpenSquareKafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.morris.opensquare.utils.Constants.TASK_STATUS_SEND_KAFKA;

@Service
public class OpenSquareKafkaProducerServiceImpl implements OpenSquareKafkaProducerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenSquareKafkaProducerServiceImpl.class);

    private final KafkaTemplate<String, OpenSquareTaskStatus> kafkaTemplate;

    @Autowired
    OpenSquareKafkaProducerServiceImpl(KafkaTemplate<String, OpenSquareTaskStatus> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(@NonNull String topicName, @NonNull String key, @NonNull OpenSquareTaskStatus value) {
        CompletableFuture<SendResult<String, OpenSquareTaskStatus>> future = kafkaTemplate.send(topicName, key, value);
        future.whenComplete((sendResult, exception) -> {
            if (exception != null) {
                future.completeExceptionally(exception);
            } else {
                future.complete(sendResult);
            }
            LOGGER.info(TASK_STATUS_SEND_KAFKA, value);
        });
    }
}
