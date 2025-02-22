package project.studyproject.domain.notification.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.internal.http2.ErrorCode;

@Getter
@RequiredArgsConstructor
public class NotificationException extends RuntimeException {
    private final ErrorCode errorCode;
}
