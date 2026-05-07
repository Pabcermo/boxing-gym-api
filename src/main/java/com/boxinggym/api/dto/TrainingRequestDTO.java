package com.boxinggym.api.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// DTO de ENTRADA: lo que el usuario manda para pedir un plan de IA
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingRequestDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El objetivo es obligatorio")
    private String goal;
    // Ej: "quiero mejorar mi resistencia para pelear 6 rounds"
}