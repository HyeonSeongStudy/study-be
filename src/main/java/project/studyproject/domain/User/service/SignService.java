package project.studyproject.domain.User.service;

import project.studyproject.domain.User.dto.UserResponse;

public interface SignService {

    UserResponse signUp(String id, String password, String name, String role);

    UserResponse signIn(String id, String password);
}
