package com.boxinggym.api.controller;
import com.boxinggym.api.dto.SessionDTO;
import com.boxinggym.api.dto.SessionResponseDTO;
import com.boxinggym.api.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    // POST /sessions
    @PostMapping
    public ResponseEntity<SessionResponseDTO> createSession(
            @Valid @RequestBody SessionDTO dto) {

        SessionResponseDTO response = sessionService.createSession(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);    // 201
    }

    // GET /sessions  →  solo sesiones futuras con cupos disponibles
    @GetMapping
    public ResponseEntity<List<SessionResponseDTO>> getAvailableSessions() {
        List<SessionResponseDTO> sessions = sessionService.getAvailableSessions();
        return ResponseEntity.ok(sessions);     // 200
    }
}