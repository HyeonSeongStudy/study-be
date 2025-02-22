package project.studyproject.domain.User.dto;

import lombok.Getter;

@Getter
public record UserInfo(
        Long userId,
        String username
        ,String name
) {


}
