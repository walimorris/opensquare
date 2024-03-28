package com.morris.opensquare.controllers;

import com.morris.opensquare.models.kafka.OpenSquareTaskStatus;
import com.morris.opensquare.services.kafka.OpenSquareKafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("opensquare/api/tasks")
public class KafkaController {

    private final OpenSquareKafkaConsumerService consumerService;

    @Autowired
    public KafkaController(OpenSquareKafkaConsumerService openSquareKafkaConsumerService) {
        this.consumerService = openSquareKafkaConsumerService;
    }

    @GetMapping("{taskId}/progress")
    public ResponseEntity<OpenSquareTaskStatus> processAsync(@PathVariable String taskId) {
        OpenSquareTaskStatus taskStatus = consumerService.getLatestTaskStatus(taskId);
        if (taskStatus == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(taskStatus);
    }
}
