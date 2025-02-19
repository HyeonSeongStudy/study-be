package project.studyproject.domain.User.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.studyproject.domain.User.dto.SignInRequest;
import project.studyproject.domain.User.dto.SignUpRequest;
import project.studyproject.domain.User.entity.RefreshUser;
import project.studyproject.domain.User.repository.RefreshRepository;
import project.studyproject.domain.User.service.SignService;
import project.studyproject.global.security.jwt.JWTUtil;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class ApiV1UserController {
    private final SignService signService;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @PostMapping("/signUp")
    public void userSignUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info("[sign-up] 회원가입 시작");
        log.info("[sign-up] id : {}, password : {}, name : {}", signUpRequest.id(), signUpRequest.password(), signUpRequest.name());
        signService.signUp(signUpRequest.id(), signUpRequest.password(), signUpRequest.name());
        log.info("[sing-up] 회원가입 완료");
    }

    @PostMapping("/signIn")
    public void userSingIn (@Valid @RequestBody SignInRequest signInRequest) {
        log.info("[signIn] 로그인 시작");
        signService.signIn(signInRequest.id(), signInRequest.password());
        log.info("[signIn] 로그인 완료");
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefreshToken(refresh);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 600000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefreshToken(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshUser refreshEntity = new RefreshUser();
        refreshEntity.setUsername(username);
        refreshEntity.setRefreshToken(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

}
