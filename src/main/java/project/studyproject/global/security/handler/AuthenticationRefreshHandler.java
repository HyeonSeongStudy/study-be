package project.studyproject.global.security.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.entity.Type;
import project.studyproject.domain.User.repository.RefreshRepository;
import project.studyproject.global.security.jwt.JWTUtil;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationRefreshHandler {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final TokenResponseUtil tokenResponseUtil;

    public void refreshHandler(HttpServletRequest request, HttpServletResponse response) {

        String refresh = tokenResponseUtil.getCookie(request, response);

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
        String newAccess = jwtUtil.createToken("access", username, role, Type.LOCAL,600000L);
        String newRefresh = jwtUtil.createToken("refresh", username, role, Type.LOCAL ,600000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefreshToken(refresh);
        tokenResponseUtil.addRefreshEntity(username, newRefresh, 86400000L);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(tokenResponseUtil.createCookie("refresh", newRefresh));

    }
}
