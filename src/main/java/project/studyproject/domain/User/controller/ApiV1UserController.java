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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.studyproject.domain.User.dto.SignInRequest;
import project.studyproject.domain.User.dto.SignInResponse;
import project.studyproject.domain.User.dto.SignUpRequest;
import project.studyproject.domain.User.entity.RefreshUser;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.repository.RefreshRepository;
import project.studyproject.domain.User.service.SignService;
import project.studyproject.global.security.handler.AuthenticationRefreshHandler;
import project.studyproject.global.security.handler.AuthenticationSuccessHandler;
import project.studyproject.global.security.jwt.JWTUtil;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiV1UserController {
    private final SignService signService;
    private final AuthenticationRefreshHandler authenticationRefreshHandler;

    @PostMapping("/signUp")
    public void userSignUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info("[sign-up] 회원가입 시작");
        log.info("[sign-up] id : {}, password : {}, name : {}", signUpRequest.id(), signUpRequest.password(), signUpRequest.name());
        signService.signUp(signUpRequest.id(), signUpRequest.password(), signUpRequest.name());
        log.info("[sing-up] 회원가입 완료");
    }

    @PostMapping("/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        authenticationRefreshHandler.refreshHandler(request, response);
    }

}
