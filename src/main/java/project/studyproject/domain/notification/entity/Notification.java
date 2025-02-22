package project.studyproject.domain.notification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter

public class Notification {
    @Id
    @GeneratedValue
    private Long id;

}
