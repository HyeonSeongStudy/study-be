package project.studyproject.domain.User.dto;

public record SignInRequest(
        String id,
        String password
) {
}
