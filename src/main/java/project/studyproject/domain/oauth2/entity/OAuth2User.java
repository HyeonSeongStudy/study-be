package project.studyproject.domain.oauth2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.entity.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String name;

    private String providerId;

    private Role role;

    public static OAuth2User oauth2From (String username, String name, Role role){
        return OAuth2User.builder()
                .username(username)
                .name(name)
                .role(role)
                .build();
    }


}
