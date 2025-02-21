package project.studyproject.global.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.entity.User;
import project.studyproject.global.security.auth.CustomUserDetails;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Jwt를 검증하고, 사용자 정보를 SecurityContext에 등록하는 역할
 */
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    // 접근 제한자
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.resolveToken(request);
        log.info("[JWTFilter] Extracted token: {}", accessToken);

        // 토큰이 없으면 다음 필터로 진행
        if (accessToken == null || jwtUtil.isExpired(accessToken)) {
            log.warn("[JWTFilter] No valid token found, proceeding without authentication.");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("[JWTFilter] Token validation started.");
        Authentication authentication = jwtUtil.getAuthentication(accessToken);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[JWTFilter] Token validation successful. User authenticated: {}", authentication.getName());
        }

        filterChain.doFilter(request, response);

    }
}
