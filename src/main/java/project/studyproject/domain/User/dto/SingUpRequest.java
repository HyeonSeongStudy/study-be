package project.studyproject.domain.User.dto;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

@Getter
public record SingUpRequest(
        String id,
        String password,
        String name
) {


}
