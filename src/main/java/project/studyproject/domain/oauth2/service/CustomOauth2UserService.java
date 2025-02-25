package project.studyproject.domain.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.oauth2.dto.*;
import project.studyproject.domain.User.entity.User;
import project.studyproject.domain.User.repository.UserRepository;

// oauth2userservice의 구현체인 default 사용해도됨
// jwt 사용 시 핸들러가 필수임
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("[사용자 정보] : {}", oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId(); // 우리 서버에서 관리 가능하게 만듬
        User existData = userRepository.getByUsername(username);
        if (existData == null) {

            User userEntity = User.oauth2From(
                    username, "", Role.Client
            );

            userRepository.save(userEntity);

            UserInfo userInfo = UserInfo.of(userEntity);

            return new CustomOAuth2User(userInfo);
        }
        else {

            existData.setName(username);
            existData.setUsername(oAuth2Response.getEmail());

            userRepository.save(existData);

            UserInfo userInfo = UserInfo.of(existData);

            //추후 작성
            return new CustomOAuth2User(userInfo);
        }
    }

}
