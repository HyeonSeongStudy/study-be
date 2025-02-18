package project.studyproject.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import project.studyproject.domain.User.dto.EntryPointErrorResponse;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 리다이렉트가 아닌 직접 Response를 생성해서 클라이언트에게 응답하는 방식으로 구현
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("[commence] 인증 실패로 response.sendError 발생");

        EntryPointErrorResponse entryPointErrorResponse = new EntryPointErrorResponse();
        entryPointErrorResponse.setMessage("인증이 실패했습니다.");
        log.error("인증 실패: {}", authException.getMessage());


        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(entryPointErrorResponse));
    }
}
