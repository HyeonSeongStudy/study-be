package project.studyproject.domain.User.service;

import project.studyproject.domain.User.dto.SignInResponse;

public interface SignService {

    void signUp(String id, String password, String name);

    SignInResponse signIn(String id, String password);
}
