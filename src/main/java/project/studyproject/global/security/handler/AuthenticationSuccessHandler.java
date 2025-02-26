package project.studyproject.global.security.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.entity.Type;
import project.studyproject.global.security.auth.CustomUserDetails;
import project.studyproject.global.security.jwt.JWTUtil;

import java.util.Collection;

/**
 * 인증 성공시 토큰을 발급해주는 역할
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final TokenResponseUtil tokenResponseUtil;

    public void successHandler(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 첫 번째 권한 가져오기
        Role role = Role.valueOf(authorities.stream().findFirst().map(GrantedAuthority::getAuthority).orElse(null));

        // 토큰 생성
        String accessToken = jwtUtil.createToken("access", username, role, Type.LOCAL ,60000L);
        String refreshToken = jwtUtil.createToken("refresh", username, role, Type.LOCAL,86400000L);

        // Refresh 토큰 저장
        tokenResponseUtil.addRefreshEntity(username, refreshToken, 86400000L);

        // 응답 설정
        response.setHeader("access", accessToken);
        response.addCookie(tokenResponseUtil.createCookie("refresh", refreshToken));

        log.info("🔹 Access Token: {}", accessToken);
        log.info("🔹 Refresh Token: {}", refreshToken);
    }
}
