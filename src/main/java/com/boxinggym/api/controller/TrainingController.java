package com.boxinggym.api.controller;
import com.boxinggym.api.dto.TrainingRequestDTO;
import com.boxinggym.api.dto.TrainingResponseDTO;
import com.boxinggym.api.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    // POST /training/recommendation
    @PostMapping("/recommendation")
    public ResponseEntity<TrainingResponseDTO> generateRecommendation(
            @Valid @RequestBody TrainingRequestDTO dto) {

        TrainingResponseDTO response = trainingService.generatePlan(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);    // 201
    }
}
