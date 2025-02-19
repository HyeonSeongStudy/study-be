package project.studyproject.domain.User.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.studyproject.domain.User.dto.SignInResponse;
import project.studyproject.domain.User.entity.User;
import project.studyproject.domain.User.repository.UserRepository;
import project.studyproject.domain.User.service.SignService;
import project.studyproject.global.security.JwtTokenProvider;

import java.util.Collection;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignServiceImpl implements SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void signUp(String id, String password, String name) {
        log.info("[signUp] 회원가입 시작");
        if (userRepository.existsByUsername(id)){
            log.info("[signUp] 아이디 중복");
            return;
        }

        /**
         * 검증 로직
         */
        SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        User user = User.from(id, passwordEncoder.encode(password), name);
        user.addAdminAuthority();

        userRepository.save(user);
    }

    @Override
    public SignInResponse signIn(String id, String password) {
        log.info("[singIn] 로그인 시작");
        User user = userRepository.getByUsername(id);
        log.info("[signIn] 회원정보 가져오기");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException();
        }
        log.info("[signIn] 비밀번호 일치");

        String token = jwtTokenProvider.createToken(String.valueOf(user.getUsername()), user.getRole());

        log.info("[signIn] 해당 토큰 : {}", token);

        return SignInResponse.of(token);
    }

}
