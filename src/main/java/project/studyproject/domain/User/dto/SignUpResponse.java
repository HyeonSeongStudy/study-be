package project.studyproject.domain.User.dto;

public record SignUpResponse(

        boolean success,
        int code,
        String msg
) {
}
