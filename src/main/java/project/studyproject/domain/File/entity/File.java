package project.studyproject.domain.File.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class File {
    @Id
    @GeneratedValue
    private int fileId;
}
