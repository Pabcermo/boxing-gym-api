package com.boxinggym.api.service;

import com.boxinggym.api.dto.TrainingRequestDTO;
import com.boxinggym.api.dto.TrainingResponseDTO;
import com.boxinggym.api.model.TrainingPlan;
import com.boxinggym.api.model.User;
import com.boxinggym.api.repository.TrainingPlanRepository;
import com.boxinggym.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final UserRepository userRepository;
    private final WebClient openAIWebClient;    // Lo configuramos en el siguiente paso

    @Value("${openai.model}")
    private String model;

    public TrainingResponseDTO generatePlan(TrainingRequestDTO dto) {

        // 1. Obtener el usuario para personalizar el prompt
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        // 2. Construir el prompt personalizado
        String prompt = buildPrompt(user, dto.getGoal());

        // 3. Llamar a OpenAI
        String generatedPlan = callOpenAI(prompt);

        // 4. Guardar el plan en BD
        TrainingPlan plan = new TrainingPlan();
        plan.setUser(user);
        plan.setGoal(dto.getGoal());
        plan.setGeneratedPlan(generatedPlan);

        TrainingPlan saved = trainingPlanRepository.save(plan);
        return toResponseDTO(saved);
    }

    private String buildPrompt(User user, String goal) {
        return String.format("""
                Eres un entrenador de boxeo profesional.
                Crea un plan de entrenamiento personalizado para:
                
                - Nombre: %s
                - Nivel: %s
                - Objetivo: %s
                
                El plan debe incluir:
                1. Calentamiento (10 min)
                2. Técnica específica de boxeo
                3. Trabajo físico (cardio, fuerza)
                4. Sparring o sacos
                5. Enfriamiento
                
                Duración total: 1 hora. Sé específico con series, repeticiones y tiempos.
                """,
                user.getName(),
                user.getLevel().name(),
                goal
        );
    }

    private String callOpenAI(String prompt) {
        // Construimos el body del request siguiendo el formato de OpenAI
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 1000
        );

        // Llamada HTTP a la API de OpenAI
        Map response = openAIWebClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();   // block() convierte la llamada reactiva en síncrona

        // Extraemos el texto de la respuesta
        // Estructura: response.choices[0].message.content
        List<Map> choices = (List<Map>) response.get("choices");
        Map message = (Map) choices.get(0).get("message");
        return (String) message.get("content");
    }

    private TrainingResponseDTO toResponseDTO(TrainingPlan plan) {
        return new TrainingResponseDTO(
                plan.getId(),
                plan.getUser().getId(),
                plan.getUser().getName(),
                plan.getGoal(),
                plan.getGeneratedPlan(),
                plan.getCreatedAt()
        );
    }
}