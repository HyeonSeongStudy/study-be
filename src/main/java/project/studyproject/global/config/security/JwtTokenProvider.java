package project.studyproject.global.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.service.UserDetailService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component // 애플케이션이 가동되면서 빈으로 자동 주입
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final UserDetailService userDetailService;

    // 시크릿 키 가져오기
    private String secretKey = "secretKeydasdadasdqudhiw189280391283814djskad";

    // 유효기간
    private final Long tokenValidityInSeconds = 1000L * 60 * 60 * 24 * 7;

    /**
     * 해당 객체가 빈 객체로 주입된 이후 수행되는 메서드를 가르킴
     * 가동 -> 빈으로 자동 주입 -> init 메서드 자동 실행
     * secretKey를 base64 형식으로 인코딩
     */
    @PostConstruct
    protected void init() {
        log.info("[init] JwtTokenProvider 내 시크릿 키 초기화 시작");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        log.info("[init] JwtTokenProvider 내 시크릿 키 초기화 완료");
    }

    /**
     * JWT 토큰 내용에 값을 넣기 위해 Claims 객체 생성
     * sub 속성에 값을 넣기 위해 고유값인 uid 넣어야함
     * 해당 토큰 사용하는 사용자의 권한 확인 role 값 추가
     *
     * @param userUid
     * @param role
     * @return
     */
    public String createToken(String userUid, Role role) {
        log.info("[createToken] 토큰 생성 시작");

        Claims claims = Jwts.claims().setSubject(userUid);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidityInSeconds))
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘
                .compact();

        log.info("[createToken] 토큰 생성 완료");
        return token;
    }

    /**
     * 필터에서 인증이 성공했을 때 SecurityContextHolder에 저장할 인가를 생성
     * 토큰 클래스를 사용하려면 초기화를 위한 User가 필요함
     *
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        UserDetails user = userDetailService.loadUserByUsername(this.getUserName(token));
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserName : {}", user.getUsername());
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    /**
     * User Detail 가져옴
     *
     * @param token
     * @return
     */
    public String getUserName(String token) {
        log.info("[getUserName] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        log.info("[getUserName] 토큰 기반 회원 구별 정보 추출 완료, {}", info);
        return info;

    }

    /**
     * 클라이언트가 헤더를 통해 애플리케이션 서버로 JWT 토큰 값을 전다랳야 정상적인 추출이 가능하다.
     *
     * @param request
     * @return
     */
    public String resolveToken(HttpServletRequest request) {
        log.info("[resolveToken] Http 헤더에서 Token 값 추출");
        return request.getHeader("X-Auth-Token");
    }

    /**
     * 토큰을 전달받아 클레임의 유효 기간을 체크하고 boolean 타입의 값을 리턴하는 역할을 한다.
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        log.info("[validateToken] 토큰 유효 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }


}
