package com.boxinggym.api.repository;
import com.boxinggym.api.model.Session;
import com.boxinggym.api.model.Session.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    // Sesiones con cupos disponibles (mayor a 0)
    List<Session> findByAvailableSpotsGreaterThan(Integer spots);

    // Sesiones por tipo (BOXING, CARDIO, SPARRING)
    List<Session> findByType(SessionType type);

    // Sesiones futuras con cupos (útil para mostrar al usuario)
    // Aquí usamos @Query cuando el nombre del método sería demasiado largo
    @Query("SELECT s FROM Session s WHERE s.date > :now AND s.availableSpots > 0 ORDER BY s.date ASC")
    List<Session> findUpcomingAvailableSessions(LocalDateTime now);
}