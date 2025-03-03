package project.studyproject.domain.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AfterEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
