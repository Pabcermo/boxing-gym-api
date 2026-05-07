package com.boxinggym.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;           // Fecha y hora de la sesión

    @Column(nullable = false)
    private Integer capacity;             // Cuántos alumnos puede tener

    @Column
    private String coach;                 // Nombre del entrenador (opcional por ahora)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionType type;

    @Column(name = "available_spots")
    private Integer availableSpots;       // Cupos disponibles (capacity - bookings)

    @PrePersist
    public void prePersist() {
        this.availableSpots = this.capacity;  // Al crear, todos los cupos están libres
    }

    public enum SessionType {
        BOXING,
        CARDIO,
        SPARRING
    }
}
