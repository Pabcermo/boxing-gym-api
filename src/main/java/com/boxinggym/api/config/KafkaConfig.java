package com.boxinggym.api.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String BOOKING_TOPIC = "booking-events";

    @Bean
    public NewTopic bookingTopic() {
        return TopicBuilder.name(BOOKING_TOPIC)
                .partitions(3)      // 3 particiones para paralelismo
                .replicas(3)        // 3 réplica
                .build();
    }
}