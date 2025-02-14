package project.studyproject.domain.User.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import project.studyproject.domain.User.dto.UserResponse;

public interface UserDetailService {
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
