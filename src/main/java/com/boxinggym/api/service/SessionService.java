package com.boxinggym.api.service;

import com.boxinggym.api.dto.SessionDTO;
import com.boxinggym.api.dto.SessionResponseDTO;
import com.boxinggym.api.model.Session;
import com.boxinggym.api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionResponseDTO createSession(SessionDTO dto) {
        Session session = new Session();
        session.setDate(dto.getDate());
        session.setCapacity(dto.getCapacity());
        session.setCoach(dto.getCoach());
        session.setType(dto.getType());
        // availableSpots se setea en @PrePersist del modelo

        Session saved = sessionRepository.save(session);
        return toResponseDTO(saved);
    }

    // Devuelve solo sesiones futuras con cupos disponibles
    public List<SessionResponseDTO> getAvailableSessions() {
        return sessionRepository
                .findUpcomingAvailableSessions(LocalDateTime.now())
                .stream()
                .map(this::toResponseDTO)   // convierte cada Session → SessionResponseDTO
                .collect(Collectors.toList());
    }

    // Lo usará BookingService para verificar y descontar cupos
    public Session getSessionEntityById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Sesión no encontrada con id: " + id));
    }

    public void decrementAvailableSpots(Session session) {
        if (session.getAvailableSpots() <= 0) {
            throw new IllegalStateException("No hay cupos disponibles en esta sesión");
        }
        session.setAvailableSpots(session.getAvailableSpots() - 1);
        sessionRepository.save(session);
    }

    public void incrementAvailableSpots(Session session) {
        session.setAvailableSpots(session.getAvailableSpots() + 1);
        sessionRepository.save(session);
    }

    private SessionResponseDTO toResponseDTO(Session session) {
        return new SessionResponseDTO(
                session.getId(),
                session.getDate(),
                session.getCapacity(),
                session.getAvailableSpots(),
                session.getCoach(),
                session.getType()
        );
    }
}
