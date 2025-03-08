package project.studyproject.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
        // 경로 별로 다른 필터 적용
        String path = request.getRequestURI();
        if (path.startsWith("/login") || path.startsWith("/oauth2")) {
            log.info("[JWTFilter] Skipping JWT validation for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 추출
        String accessToken = jwtUtil.resolveToken(request);
        log.info("[JWTFilter] Extracted token: {}", accessToken);

        // 토큰이 없으면 다음 필터로 진행
        if (accessToken == null || jwtUtil.isExpired(accessToken)) {
            log.warn("[JWTFilter] No valid token found, proceeding without authentication.");
            filterChain.doFilter(request, response);
            return;
        }



        // 토큰 유효성 확인 ( OAuth2 와 Local을 분리해아함)
        log.info("[JWTFilter] Token validation started.");
        Authentication authentication = jwtUtil.getAuthentication(accessToken);



        // 토큰 유효할 경우 User의 권한을 발급
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[JWTFilter] Token validation successful. User authenticated: {}", authentication.getName());
        }

        filterChain.doFilter(request, response);

    }
}

// 웹 앱이랑 다르다.
// 이건 웹임.
// AOP
// API당 시간, 응답, 쿼리 갯수
