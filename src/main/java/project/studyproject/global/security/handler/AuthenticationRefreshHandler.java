package project.studyproject.global.security.handler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import project.studyproject.domain.User.entity.RefreshUser;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.repository.RefreshRepository;
import project.studyproject.global.security.jwt.JWTUtil;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationRefreshHandler {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public void refreshHandler (HttpServletRequest request, HttpServletResponse response){

        String refresh = getCookie(request, response);

        if (jwtUtil.isExpired(refresh)) {
            log.info("[토큰 만료] 해당 토큰 만료되었습니다.");
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            log.info("[토큰 오류] 해당 토큰은 refreshToken 아닙니다.");
            return;
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefreshToken(refresh);
        if (!isExist) {
            log.info("[토큰 저장 X] 해당 토큰은 DB에 저장되어 있지 않습니다.");
            return;
        }

        String username = jwtUtil.getUsername(refresh);
        Role role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createToken("access", username, role, 600000L);
        String newRefresh = jwtUtil.createToken("refresh", username, role, 600000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefreshToken(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

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

        if (refresh == null) {

            //response status code
            return "오류" + HttpStatus.BAD_REQUEST;
        }

        return refresh;
    }
}
