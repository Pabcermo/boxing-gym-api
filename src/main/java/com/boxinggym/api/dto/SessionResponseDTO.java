package com.boxinggym.api.dto;
import com.boxinggym.api.model.Session.SessionType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// DTO de SALIDA: lo que devuelve la API al consultar sesiones
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponseDTO {

    private Long id;
    private LocalDateTime date;
    private Integer capacity;
    private Integer availableSpots;     // Cupos disponibles en tiempo real
    private String coach;
    private SessionType type;
}