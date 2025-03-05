package project.studyproject.domain.User.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Member")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public static User localFrom (String username, String password, String name) {
        return User.builder()
                .username(username)
                .password(password)
                .name(name)
                .role(Role.Client)
                .build();
    }

    public static User oauth2From (String username,String name, Role role){
        return User.builder()
                .username(username)
                .name(name)
                .role(role)
                .build();
    }

    //== 회원가입시, USER의 권한을 부여 ==//
    public void addUserAuthority() {
        this.role = Role.Client;
    }

    public void addAdminAuthority() {
        this.role = Role.Admin;
    }
}
