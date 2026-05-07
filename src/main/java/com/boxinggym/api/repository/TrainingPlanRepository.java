package com.boxinggym.api.repository;
import com.boxinggym.api.model.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {

    // Todos los planes de entrenamiento de un usuario
    List<TrainingPlan> findByUserIdOrderByCreatedAtDesc(Long userId);
    // ORDER BY created_at DESC → el más reciente primero
}
