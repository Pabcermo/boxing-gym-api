package com.boxinggym.api.repository;
import com.boxinggym.api.model.Booking;
import com.boxinggym.api.model.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Todas las reservas de un usuario
    List<Booking> findByUserId(Long userId);

    // Reservas de un usuario filtradas por estado
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);

    // Verificar si ya existe una reserva para ese usuario y sesión
    // (evita duplicados a nivel de servicio, además del constraint en DB)
    boolean existsByUserIdAndSessionId(Long userId, Long sessionId);

    // Buscar reserva específica de un usuario en una sesión
    Optional<Booking> findByUserIdAndSessionId(Long userId, Long sessionId);

    // Cuántas reservas confirmadas tiene una sesión (para controlar capacidad)
    long countBySessionIdAndStatus(Long sessionId, BookingStatus status);
}
