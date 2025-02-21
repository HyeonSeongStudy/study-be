package project.studyproject.global.security.handler;

public class JwtExceptionHandler extends RuntimeException {
    public JwtExceptionHandler(String message) {
        super(message);
    }
}
