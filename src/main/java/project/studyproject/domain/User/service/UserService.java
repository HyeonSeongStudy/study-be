package project.studyproject.domain.User.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
