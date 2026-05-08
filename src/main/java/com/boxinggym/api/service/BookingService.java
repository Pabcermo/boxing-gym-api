package com.boxinggym.api.service;

import com.boxinggym.api.dto.BookingDTO;
import com.boxinggym.api.dto.BookingResponseDTO;
import com.boxinggym.api.event.BookingEvent;
import com.boxinggym.api.model.Booking;
import com.boxinggym.api.model.Booking.BookingStatus;
import com.boxinggym.api.model.Notification;
import com.boxinggym.api.model.Session;
import com.boxinggym.api.model.User;
import com.boxinggym.api.repository.BookingRepository;
import com.boxinggym.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final BookingEventProducer eventProducer;

    @Transactional  // Si algo falla, hace rollback completo (no queda a medias)
    public BookingResponseDTO createBooking(BookingDTO dto) {

        // 1. Verificar que el usuario existe
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        // 2. Verificar que la sesión existe
        Session session = sessionService.getSessionEntityById(dto.getSessionId());

        // 3. Verificar que el usuario no tiene ya una reserva en esta sesión
        if (bookingRepository.existsByUserIdAndSessionId(dto.getUserId(), dto.getSessionId())) {
            throw new IllegalArgumentException("El usuario ya tiene una reserva en esta sesión");
        }

        // 4. Verificar y descontar cupos (lanza excepción si no hay cupos)
        sessionService.decrementAvailableSpots(session);

        // 5. Crear la reserva
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSession(session);
        // status se setea en @PrePersist como CONFIRMED

        Booking saved = bookingRepository.save(booking);

        // ← Publicar evento de reserva confirmada
        eventProducer.publishEvent(new BookingEvent(
                saved.getId(),
                user.getId(),
                user.getName(),
                session.getId(),
                session.getDate(),
                session.getType().name(),
                Notification.NotificationType.BOOKING_CONFIRMED,
                LocalDateTime.now()
        ));
        return toResponseDTO(saved);
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Reserva no encontrada"));

        // No cancelar algo que ya está cancelado
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("La reserva ya está cancelada");
        }

        // Devolver el cupo a la sesión
        sessionService.incrementAvailableSpots(booking.getSession());

        // Cambiar estado
        booking.setStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);
        // ← Publicar evento de reserva cancelada
        eventProducer.publishEvent(new BookingEvent(
                saved.getId(),
                saved.getUser().getId(),
                saved.getUser().getName(),
                saved.getSession().getId(),
                saved.getSession().getDate(),
                saved.getSession().getType().name(),
                Notification.NotificationType.BOOKING_CANCELLED,
                LocalDateTime.now()
        ));


        return toResponseDTO(saved);
    }

    public List<BookingResponseDTO> getBookingsByUser(Long userId) {
        // Verificar que el usuario existe
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("Usuario no encontrado con id: " + userId);
        }

        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private BookingResponseDTO toResponseDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getUser().getId(),
                booking.getUser().getName(),
                booking.getSession().getId(),
                booking.getSession().getDate(),
                booking.getSession().getType().name(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }
}
