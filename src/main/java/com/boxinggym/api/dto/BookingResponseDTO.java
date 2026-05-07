package com.boxinggym.api.dto;
import com.boxinggym.api.model.Booking.BookingStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// DTO de SALIDA: confirmación de reserva
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {

    private Long id;
    private Long userId;
    private String userName;        // Nombre del usuario (no solo el ID)
    private Long sessionId;
    private LocalDateTime sessionDate;
    private String sessionType;
    private BookingStatus status;
    private LocalDateTime createdAt;
}