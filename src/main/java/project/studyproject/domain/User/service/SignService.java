package project.studyproject.domain.User.service;

import org.springframework.security.core.Authentication;
import project.studyproject.domain.User.dto.SignInResponse;

public interface SignService {

    void signUp(String id, String password, String name);
}
