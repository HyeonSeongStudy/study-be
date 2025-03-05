package project.studyproject.domain.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.studyproject.domain.oauth2.dto.GoogleResponse;
import project.studyproject.domain.oauth2.dto.NaverResponse;
import project.studyproject.domain.oauth2.dto.OAuth2Response;
import project.studyproject.domain.oauth2.dto.CustomOAuth2User;
import project.studyproject.domain.User.entity.Role;
import project.studyproject.domain.oauth2.dto.*;
import project.studyproject.domain.User.entity.User;
import project.studyproject.domain.User.repository.UserRepository;

// oauth2userservice의 구현체인 default 사용해도됨
// jwt 사용 시 핸들러가 필수임
// 여기 이제 OAuth2User 해야함
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    // 해당 유저 정보 가져오기
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("[사용자 정보] : {}", oAuth2User.getAttributes());

        // 어떤 사이트에서 가져오는지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }

        // 서버에서 관리 용이하게 임의로 username을 생성
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        // 이미 저장되어있는 데이터 가져오기
        // 있다면 새롭게 적용, 없다면 새로운 걸 적용
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
