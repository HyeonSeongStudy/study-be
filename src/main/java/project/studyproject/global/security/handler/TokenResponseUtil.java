package project.studyproject.global.security.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import project.studyproject.domain.User.entity.RefreshUser;
import project.studyproject.domain.User.repository.RefreshRepository;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenResponseUtil {

    private final RefreshRepository refreshRepository;

    /**
     * RefreshRepo에 저장하기 위해 사용
     *
     * @param username
     * @param refresh
     * @param expiredMs
     */
    public void addRefreshEntity(String username, String refresh, Long expiredMs) {

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
     *
     * @param key
     * @param value
     * @return
     */
    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public String getCookie(HttpServletRequest request, HttpServletResponse response) {
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

    public Object getPrincipal(HttpServletRequest request) {
        return request.getUserPrincipal();
    }


}
