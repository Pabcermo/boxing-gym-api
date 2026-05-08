package com.boxinggym.api.service;

import com.boxinggym.api.config.KafkaConfig;
import com.boxinggym.api.event.BookingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingEventProducer {

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public void publishEvent(BookingEvent event) {
        log.info("Publicando evento Kafka: {} para usuario: {}",
                event.getEventType(), event.getUserId());

        kafkaTemplate.send(
                KafkaConfig.BOOKING_TOPIC,
                event.getUserId().toString(),   // key = userId (misma partición por usuario)
                event
        );

        log.info("Evento publicado correctamente en topic: {}", KafkaConfig.BOOKING_TOPIC);
    }
}