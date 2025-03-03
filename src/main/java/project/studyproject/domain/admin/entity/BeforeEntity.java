package project.studyproject.domain.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BeforeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
