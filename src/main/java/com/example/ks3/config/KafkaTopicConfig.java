package com.example.ks3.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.position-instrument}")
    private String positionInstrumentTopic;

    @Value("${kafka.topic.gold-smc}")
    private String goldSmcTopic;

    @Value("${kafka.topic.output}")
    private String outputTopic;

    @Bean
    public NewTopic positionInstrumentTopic() {
        return TopicBuilder.name(positionInstrumentTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic goldSmcTopic() {
        return TopicBuilder.name(goldSmcTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic outputTopic() {
        return TopicBuilder.name(outputTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
} 