package project.studyproject.domain.oauth2.entity;

import org.springframework.security.core.GrantedAuthority;
import project.studyproject.domain.User.entity.Role;

import java.util.Collection;

public interface CustomUserPrincipal {
    String getUsername();
    Collection<? extends GrantedAuthority> getAuthorities();
    Role getRole();
}
