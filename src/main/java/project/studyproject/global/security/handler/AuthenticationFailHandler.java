package project.studyproject.global.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * 유저네임과 패스워드 인증 실패 시
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFailHandler {

    public void failHandle(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
