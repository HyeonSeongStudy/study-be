package project.studyproject.global.security.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.entity.Type;
import project.studyproject.domain.oauth2.service.CustomOauth2UserService;
import project.studyproject.global.security.auth.CustomUserDetailService;
import project.studyproject.global.security.auth.CustomUserDetails;
import project.studyproject.global.security.handler.JwtExceptionHandler;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 생성 및 검증을 진행
 * providerManager 역할함
 */
@Component
@Slf4j
public class JWTUtil {

    private final CustomUserDetailService customUserDetailService;
    private final CustomOauth2UserService customOauth2UserService;
    // 객체 키 생성
    private SecretKey secretKey;

    // 서명과 검증을 위한 시크릿 키 생성
    public JWTUtil(@Value("${spring.jwt.secret}") String secret, CustomUserDetailService customUserDetailService, CustomOauth2UserService customOauth2UserService) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.customUserDetailService = customUserDetailService;
        this.customOauth2UserService = customOauth2UserService;
    }

    // 키에서 유저네임 받아오기
    public String getUsername(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
        } catch (Exception e) {
            throw new JwtExceptionHandler("유효하지 않은 토큰입니다.");
        }
    }

    // 토큰의 타입을 가져옴
    public Type getType(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("type", Type.class);
        } catch (Exception e) {
            throw new JwtExceptionHandler("이건 뭐고?");
        }
    }

    // 토큰 인증 정보 조회 시작
    public Authentication getAuthentication(String token) {
        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
//        if (getType(token).equals(Type.LOCAL)) {
        CustomUserDetails user = (CustomUserDetails) customUserDetailService.loadUserByUsername(getUsername(token));
//        } else if (getType(token).equals(Type.OAUTH2)) {
//            CustomOAuth2User user = (CustomOAuth2User) customOauth2UserService.loadUser(token);
//        }

        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserName : {}", user.getUsername());
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    // 키에서 권한 받아오기
    public Role getRole(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", Role.class);
        } catch (Exception e) {
            throw new JwtExceptionHandler("유효하지 않은 토큰입니다.");
        }
    }

    // 무슨 토큰인지 확인하는 메서드
    public String getCategory(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
        } catch (Exception e) {
            throw new JwtExceptionHandler("유효하지 않은 토큰입니다.");
        }
    }

    // 토큰 만료 확인
    public Boolean isExpired(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            throw new JwtExceptionHandler("유효하지 않은 토큰입니다.");
        }
    }

    // 토큰 생성
    public String createToken(String category, String username, Role role, Type type, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .claim("type", type)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    // 토큰츄츌
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("access");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 값만 가져옴
        }
        return null;
    }
}
