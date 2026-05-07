package com.boxinggym.api.dto;
import com.boxinggym.api.model.User.Level;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// DTO de SALIDA: lo que la API devuelve al cliente
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Level level;
    private LocalDateTime createdAt;
}
