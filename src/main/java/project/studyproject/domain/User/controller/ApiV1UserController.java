package project.studyproject.domain.User.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.studyproject.domain.User.dto.SignUpRequest;
import project.studyproject.domain.User.service.SignService;
import project.studyproject.global.security.handler.AuthenticationRefreshHandler;

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
