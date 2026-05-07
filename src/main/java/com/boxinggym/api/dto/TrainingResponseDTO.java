package com.boxinggym.api.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// DTO de SALIDA: el plan generado por OpenAI
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingResponseDTO {

    private Long id;
    private Long userId;
    private String userName;
    private String goal;
    private String generatedPlan;   // Respuesta de OpenAI
    private LocalDateTime createdAt;
}
