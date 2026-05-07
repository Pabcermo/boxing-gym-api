package com.boxinggym.api.controller;
import com.boxinggym.api.dto.UserDTO;
import com.boxinggym.api.dto.UserResponseDTO;
import com.boxinggym.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController                 // Controller que devuelve JSON automáticamente
@RequestMapping("/users")       // Prefijo de todas las rutas de este controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /users
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserDTO dto) {
        //  ↑ activa las validaciones del DTO

        UserResponseDTO response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);    // 201
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO response = userService.getUserById(id);
        return ResponseEntity.ok(response);     // 200
    }
}