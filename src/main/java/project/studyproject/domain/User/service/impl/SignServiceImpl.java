package project.studyproject.domain.User.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.studyproject.domain.User.dto.SignInResponse;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.entity.User;
import project.studyproject.domain.User.repository.UserRepository;
import project.studyproject.domain.User.service.SignService;
import project.studyproject.global.security.auth.CustomUserDetails;
import project.studyproject.global.security.jwt.JWTUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignServiceImpl implements SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Override
    public void signUp(String id, String password, String name) {
        log.info("[signUp] 회원가입 시작");
        if (userRepository.existsByUsername(id)){
            log.info("[signUp] 아이디 중복");
            return;
        }

        User user = User.from(id, passwordEncoder.encode(password), name);
        user.addAdminAuthority();

        userRepository.save(user);
    }


}
