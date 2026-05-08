package com.boxinggym.api.service;

import com.boxinggym.api.config.KafkaConfig;
import com.boxinggym.api.event.BookingEvent;
import com.boxinggym.api.model.Notification;
import com.boxinggym.api.model.User;
import com.boxinggym.api.repository.NotificationRepository;
import com.boxinggym.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingEventConsumer {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @KafkaListener(
            topics = KafkaConfig.BOOKING_TOPIC,
            groupId = "boxing-gym-group"
    )
    public void consume(BookingEvent event) {
        log.info("Evento recibido: {} para usuario: {}",
                event.getEventType(), event.getUserId());

        // Construir mensaje legible según el tipo de evento
        String message = buildMessage(event);

        // Obtener el usuario
        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        // Guardar notificación en BD
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(event.getEventType());

        notificationRepository.save(notification);

        log.info("Notificación guardada correctamente para usuario: {}", user.getName());
    }

    private String buildMessage(BookingEvent event) {
        return switch (event.getEventType()) {
            case BOOKING_CONFIRMED -> String.format(
                    "✅ %s, tu reserva de %s el %s ha sido confirmada.",
                    event.getUserName(),
                    event.getSessionType(),
                    event.getSessionDate()
            );
            case BOOKING_CANCELLED -> String.format(
                    "❌ %s, tu reserva de %s el %s ha sido cancelada.",
                    event.getUserName(),
                    event.getSessionType(),
                    event.getSessionDate()
            );
        };
    }
}