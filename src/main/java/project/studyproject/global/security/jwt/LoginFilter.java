package project.studyproject.global.security.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.studyproject.domain.User.repository.RefreshRepository;
import project.studyproject.global.security.handler.AuthenticationFailHandler;
import project.studyproject.global.security.handler.AuthenticationRefreshHandler;
import project.studyproject.global.security.handler.AuthenticationSuccessHandler;

/**
 * 로그인 요청 시 사용자 인증을 수행, 성공하면 JWT 반환
 */
@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailHandler authenticationFailureHandler;
    private final RefreshRepository refreshRepository;

    // 로그인 인증 필터
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password, null);
        log.info("[authRequest] 인증 요청  : {}", authRequest);

        return authenticationManager.authenticate(authRequest);
    }

    // 성공 시
    // 여기서 로그인이 완료되면 토큰을 발급하는 형식
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        authenticationSuccessHandler.successHandler(request, response, chain, authentication);
    }

    // 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        authenticationFailureHandler.failHandle(request, response, failed);
    }

}
