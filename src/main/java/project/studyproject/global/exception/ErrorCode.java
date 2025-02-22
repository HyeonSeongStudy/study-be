package project.studyproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("해당 사용자 아님", 0);


    private final String msg;
    private final int errorCode;

}
