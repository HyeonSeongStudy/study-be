package project.studyproject.domain.User.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.studyproject.domain.User.dto.SignInResponse;
import project.studyproject.domain.User.entity.User;
import project.studyproject.domain.User.repository.UserRepository;
import project.studyproject.domain.User.service.SignService;
import project.studyproject.global.config.security.JwtTokenProvider;

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
        User user = User.from(id, passwordEncoder.encode(password), name);
        user.addUserAuthority();

        userRepository.save(user);
    }

    @Override
    public SignInResponse signIn(String id, String password) {
        log.info("[singIn] 로그인 시작");
        User user = userRepository.getByUid(id);
        log.info("[signIn] 회원정보 가져오기");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException();
        }
        log.info("[signIn] 비밀번호 일치");

        String token = jwtTokenProvider.createToken(String.valueOf(user.getUid()), user.getRole());

        log.info("[signIn] 해당 토큰 : {}", token);

        return SignInResponse.of(token);
    }

}
