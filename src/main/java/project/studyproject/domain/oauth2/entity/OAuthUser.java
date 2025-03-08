package project.studyproject.domain.oauth2.entity;

import jakarta.persistence.*;
import lombok.*;
import project.studyproject.domain.User.entity.Role;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일
    private String username;

    // 이름
    private String name;

    // 제공 서버 이름
    private String provider;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static OAuthUser oauth2From (String username, String name, Role role){
        return OAuthUser.builder()
                .username(username)
                .name(name)
                .role(role)
                .build();
    }


}
