package project.studyproject.domain.User.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.studyproject.domain.User.dto.SignInRequest;
import project.studyproject.domain.User.dto.SignUpRequest;
import project.studyproject.domain.User.service.SignService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class ApiV1UserController {
    private final SignService signService;

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

}
