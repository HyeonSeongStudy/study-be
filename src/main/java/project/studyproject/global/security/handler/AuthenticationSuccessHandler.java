package project.studyproject.global.security.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import project.studyproject.domain.User.entity.RefreshUser;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.repository.RefreshRepository;
import project.studyproject.global.security.auth.CustomUserDetails;
import project.studyproject.global.security.jwt.JWTUtil;

import java.util.Collection;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public void successHandler (HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 첫 번째 권한 가져오기
        String role = authorities.stream().findFirst().map(GrantedAuthority::getAuthority).orElse(null);

        // 토큰 생성
        String accessToken = jwtUtil.createToken("access", username, Role.Admin, 60000L);
        String refreshToken = jwtUtil.createToken("refresh", username, Role.Admin, 86400000L);

        // Refresh 토큰 저장
        addRefreshEntity(username, refreshToken, 86400000L);

        // 응답 설정
        response.setHeader("access", accessToken);
        response.addCookie(createCookie("refresh", refreshToken));

        log.info("🔹 Access Token: {}", accessToken);
        log.info("🔹 Refresh Token: {}", refreshToken);
    }

    /**
     * RefreshRepo에 저장하기 위해 사용
     * @param username
     * @param refresh
     * @param expiredMs
     */
    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshUser refreshEntity = new RefreshUser();
        refreshEntity.setUsername(username);
        refreshEntity.setRefreshToken(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    /**
     * key = 키 값 , value = jwt값
     * Cookie 로 저장하기 위해 생성
     * @param key
     * @param value
     * @return
     */
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private String getCookie(HttpServletRequest request,HttpServletResponse response ) {
        String refresh = null; // 여기서 원래 refresh 토큰 받아야함?

        // 리프레쉬 토큰 추출
        Cookie[] cookies = request.getCookies();
        // 리프레쉬 토큰 추출
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        return refresh;
    }
}
