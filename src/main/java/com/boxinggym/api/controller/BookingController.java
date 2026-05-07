package com.boxinggym.api.controller;
import com.boxinggym.api.dto.BookingDTO;
import com.boxinggym.api.dto.BookingResponseDTO;
import com.boxinggym.api.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // POST /bookings
    @PostMapping("/bookings")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingDTO dto) {

        BookingResponseDTO response = bookingService.createBooking(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);    // 201
    }

    // GET /users/{id}/bookings
    @GetMapping("/users/{id}/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUser(
            @PathVariable Long id) {

        List<BookingResponseDTO> bookings = bookingService.getBookingsByUser(id);
        return ResponseEntity.ok(bookings);     // 200
    }

    // PATCH /bookings/{id}/cancel
    @PatchMapping("/bookings/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id) {
        BookingResponseDTO response = bookingService.cancelBooking(id);
        return ResponseEntity.ok(response);     // 200
    }
}