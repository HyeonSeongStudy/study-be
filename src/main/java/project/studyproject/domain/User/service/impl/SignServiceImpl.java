package project.studyproject.domain.User.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.studyproject.domain.User.Repository.UserRepository;
import project.studyproject.domain.User.dto.UserResponse;
import project.studyproject.domain.User.service.SignService;
import project.studyproject.global.config.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserResponse signUp(String id, String password, String name, String role) {
        return null;
    }

    @Override
    public UserResponse signIn(String id, String password) {
        return null;
    }
}
