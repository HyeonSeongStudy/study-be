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
import project.studyproject.domain.oauth2.entity.OAuthUser;
import project.studyproject.domain.oauth2.repository.OAuthRepository;

// oauth2userservice의 구현체인 default 사용해도됨
// jwt 사용 시 핸들러가 필수임
// 여기 이제 OAuth2User 해야함
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final OAuthRepository oAuthRepository;

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
        log.info("[username] 유저 네임 : {}", username);

        // 이미 저장되어있는 데이터 가져오기
        // 있다면 새롭게 적용, 없다면 새로운 걸 적용
        OAuthUser existData = oAuthRepository.getByUsername(username);
        if (existData == null) {

            OAuthUser entity = OAuthUser.oauth2From(
                    username, "test", Role.Client
            );

            oAuthRepository.save(entity);

            // OAuthUser 을 DTO 형태로 변환
            UserInfo userInfo = UserInfo.of(entity);


            log.info("[CustomOAuth2User] 유저 네임 : {}", userInfo);
            return new CustomOAuth2User(userInfo);
        }
        else {

            OAuthUser entity = OAuthUser.oauth2From(
                    existData.getUsername(), "test", Role.Client
            );

            oAuthRepository.save(entity);

            // 엔티티 저장 위해서 + 서비스 로직 수행
            UserInfo userInfo = UserInfo.of(entity);

            //추후 작성
            // 인터페이스 타입으로 넘겨줄 DTO
            log.info("[CustomOAuth2User] 유저 네임 : {}", userInfo);
            return new CustomOAuth2User(userInfo);
        }
    }

}
