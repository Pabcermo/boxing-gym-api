package com.boxinggym.api.repository;

import com.boxinggym.api.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Todas las notificaciones de un usuario ordenadas por fecha
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Solo las no leídas
    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);
}