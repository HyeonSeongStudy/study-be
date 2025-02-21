package project.studyproject.domain.User.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RefreshUser {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    @Column(unique = true, length = 500)
    private String refreshToken;
    private String expiration;

}
