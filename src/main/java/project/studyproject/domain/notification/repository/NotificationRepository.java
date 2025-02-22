package project.studyproject.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studyproject.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
