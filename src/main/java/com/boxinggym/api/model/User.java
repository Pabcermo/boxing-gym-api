package com.boxinggym.api.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment en PostgreSQL
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)  // Email único en la tabla
    private String email;

    @Enumerated(EnumType.STRING)   // Guarda "BEGINNER" como texto, no como número
    @Column(nullable = false)
    private Level level;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist                    // Se ejecuta automáticamente antes de guardar
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Enum dentro de la misma clase (está relacionado solo con User)
    public enum Level {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }
}