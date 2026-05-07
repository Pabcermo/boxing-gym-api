package com.boxinggym.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "bookings",
        uniqueConstraints = {
                // Un usuario no puede reservar la misma sesión dos veces
                @UniqueConstraint(columnNames = {"user_id", "session_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con User: muchas reservas pueden pertenecer a un usuario
    @ManyToOne(fetch = FetchType.LAZY)   // LAZY = no carga el User hasta que lo necesites
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relación con Session: muchas reservas pueden ser de una sesión
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED;   // Por defecto, confirmada
    }

    public enum BookingStatus {
        CONFIRMED,
        CANCELLED
    }
}