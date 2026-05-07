package com.boxinggym.api.dto;
import com.boxinggym.api.model.Session.SessionType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// DTO de ENTRADA: para crear una sesión
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {

    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La sesión debe ser en el futuro")   // Valida que la fecha no sea pasada
    private LocalDateTime date;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad mínima es 1")
    private Integer capacity;

    private String coach;                                   // Opcional

    @NotNull(message = "El tipo de sesión es obligatorio")
    private SessionType type;                               // BOXING, CARDIO, SPARRING
}