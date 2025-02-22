package project.studyproject.domain.oauth2.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.studyproject.domain.oauth2.dto.OAuth2Response;
import project.studyproject.domain.User.entity.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2Response oAuth2Response;

    // 로그인하면 리소스로부터 넘어오는 모든 데이터
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(Role.Client);
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return oAuth2Response.getName();
    }

    // 유저네임을 임의로 만듬
    public String getUsername(){
        return oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    }
}
