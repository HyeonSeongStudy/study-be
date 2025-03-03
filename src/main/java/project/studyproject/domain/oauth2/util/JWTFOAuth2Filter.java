package project.studyproject.domain.oauth2.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.entity.User;
import project.studyproject.domain.oauth2.dto.CustomOAuth2User;
import project.studyproject.domain.oauth2.dto.UserInfo;
import project.studyproject.global.security.jwt.JWTUtil;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JWTFOAuth2Filter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            System.out.println(cookie.getName());
            if (cookie.getName().equals("Authorization")) {

                authorization = cookie.getValue();
            }
        }

        // 헤더 검증

        //토큰
        // 쿠키에서 가져온 토큰 값임
        String token = authorization;


        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        Role role = jwtUtil.getRole(token);

        User aUser = User.oauth2From(username, "", role);

        //userDTO를 생성하여 값 set
        UserInfo user = UserInfo.of(aUser);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(user);




        //스프링 시큐리티 인증 토큰 생성
        // 그냥 셋터 느낌임
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());



        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
