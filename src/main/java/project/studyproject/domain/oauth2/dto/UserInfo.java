package project.studyproject.domain.oauth2.dto;


import lombok.Builder;
import lombok.Getter;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.entity.User;

@Builder
public record UserInfo(
        String username,
        String name,
        Role role
) {
    public static UserInfo of (User user){
        return UserInfo.builder()
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
