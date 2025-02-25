package project.studyproject.global.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.studyproject.domain.User.entity.User;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * 권한 여부
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_" + user.getRole();
            }
        });
        return collection;
    }

    /**
     * 패스워드 가져오기
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 이메일 가져오기
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
