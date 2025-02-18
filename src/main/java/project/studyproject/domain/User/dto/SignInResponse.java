package project.studyproject.domain.User.dto;

public record SignInResponse(
       String token
) {
    public static SignInResponse of(String token) {
        return new SignInResponse(token);
    }
}
