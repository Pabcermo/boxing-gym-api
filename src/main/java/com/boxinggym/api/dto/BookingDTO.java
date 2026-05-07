package com.boxinggym.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// DTO de ENTRADA: para crear una reserva
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El ID de sesión es obligatorio")
    private Long sessionId;
}