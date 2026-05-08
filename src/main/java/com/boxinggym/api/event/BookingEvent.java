package com.boxinggym.api.event;

import com.boxinggym.api.model.Notification.NotificationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingEvent {

    private Long bookingId;
    private Long userId;
    private String userName;
    private Long sessionId;
    private LocalDateTime sessionDate;
    private String sessionType;
    private NotificationType eventType;   // BOOKING_CONFIRMED o BOOKING_CANCELLED
    private LocalDateTime occurredAt;
}