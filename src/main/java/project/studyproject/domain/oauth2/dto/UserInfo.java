package project.studyproject.domain.oauth2.dto;


import lombok.Builder;
import lombok.Getter;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.User.entity.Type;
import project.studyproject.domain.User.entity.User;
import project.studyproject.domain.oauth2.entity.OAuthUser;

@Builder
public record UserInfo(
        String username,
        String name,
        Role role
) {
    public static UserInfo of (OAuthUser user){
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
