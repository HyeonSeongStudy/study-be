package project.studyproject.domain.oauth2.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.entity.Type;
import project.studyproject.domain.oauth2.dto.CustomOAuth2User;
import project.studyproject.global.security.handler.TokenResponseUtil;
import project.studyproject.global.security.jwt.JWTUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

// 성공핸들러 단일 처리 할거임

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final TokenResponseUtil tokenResponseUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws  ServletException, IOException {
        //OAuth2User
        // Local, OAuth2 구분
        // OAuth2 Handler 상속

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        // 유저 정보 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        Role role = Role.valueOf(auth.getAuthority());

        // 토큰 생성
        String accessToken = jwtUtil.createToken("access", username, role , Type.OAUTH2 ,60*60*60L);
        String refreshToken = jwtUtil.createToken("refresh", username, role , Type.OAUTH2 ,60*60*60L);



        response.setHeader("access", accessToken);
        response.addCookie(tokenResponseUtil.createCookie("refresh", refreshToken));
        response.sendRedirect("http://localhost:3000/");

        log.info("🔹 Access Token: {}", accessToken);
        log.info("🔹 Refresh Token: {}", refreshToken);
    }
}
