package com.morris.opensquare.configurations;

import com.morris.opensquare.models.kafka.OpenSquareTaskStatus;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties kafkaProperties) {
        KafkaAdmin kafkaAdmin = new KafkaAdmin(kafkaProperties.buildAdminProperties(new DefaultSslBundleRegistry()));
        kafkaAdmin.setFatalIfBrokerNotAvailable(kafkaProperties.getAdmin().isFailFast());
        return kafkaAdmin;
    }

    @Bean
    public ConsumerFactory<String, OpenSquareTaskStatus> consumerFactory() {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configurationProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        configurationProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configurationProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurationProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configurationProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(configurationProperties);
    }

    @Bean
    public KafkaConsumer<String, OpenSquareTaskStatus> kafkaConsumer() {
        return (KafkaConsumer<String, OpenSquareTaskStatus>) consumerFactory().createConsumer();
    }

    @Bean
    public ProducerFactory<String, OpenSquareTaskStatus> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, OpenSquareTaskStatus> kafkaTemplate() {
        KafkaTemplate<String, OpenSquareTaskStatus> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setConsumerFactory(consumerFactory());
        return kafkaTemplate;
    }
}
