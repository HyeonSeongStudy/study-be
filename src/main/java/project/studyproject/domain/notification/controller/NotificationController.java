package project.studyproject.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.junit.platform.commons.util.ClassUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.studyproject.domain.User.dto.UserInfo;
import project.studyproject.domain.notification.exception.NotificationException;
import project.studyproject.domain.notification.service.NotificationService;
import project.studyproject.global.exception.ErrorCode;

@RestController
@RequestMapping("/api/v1/users/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
        // Authentication을 UserDto로 업캐스팅
        UserInfo userInfo = ClassUtils.nullSafeToString(authentication.getPrincipal().getClass(), UserInfo.class);

        // 서비스를 통해 생성된 SseEmitter를 반환
        return notificationService.connectNotification(userInfo);
    }
}
