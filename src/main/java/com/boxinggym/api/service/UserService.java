package com.boxinggym.api.service;

import com.boxinggym.api.dto.UserDTO;
import com.boxinggym.api.dto.UserResponseDTO;
import com.boxinggym.api.model.User;
import com.boxinggym.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service                        // Le dice a Spring que este es un servicio
@RequiredArgsConstructor        // Lombok: genera constructor con los campos final
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserDTO dto) {
        // 1. Verificar que el email no esté registrado
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + dto.getEmail());
        }

        // 2. Convertir DTO → Modelo
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setLevel(dto.getLevel());

        // 3. Guardar en BD
        User saved = userRepository.save(user);

        // 4. Convertir Modelo → ResponseDTO y devolver
        return toResponseDTO(saved);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + id));

        return toResponseDTO(user);
    }

    // Método privado de conversión (mapper manual)
    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLevel(),
                user.getCreatedAt()
        );
    }
}