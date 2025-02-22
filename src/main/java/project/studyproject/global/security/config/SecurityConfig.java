package project.studyproject.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import project.studyproject.domain.oauth2.service.CustomOauth2UserService;
import project.studyproject.domain.User.repository.RefreshRepository;
import project.studyproject.domain.oauth2.util.CustomClientRegistrationRepo;
import project.studyproject.domain.oauth2.util.CustomOAuth2AuthorizedClientService;
import project.studyproject.global.security.filter.CustomLogoutFilter;
import project.studyproject.global.security.handler.AuthenticationFailHandler;
import project.studyproject.global.security.handler.AuthenticationSuccessHandler;
import project.studyproject.global.security.jwt.JWTFilter;
import project.studyproject.global.security.jwt.JWTUtil;
import project.studyproject.global.security.jwt.LoginFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security에 대한 디버깅 모드를 사용하기 위한 어노테이션 (default : false)
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailHandler authenticationFailureHandler;
    private final CustomOauth2UserService customOauth2UserService;
    private final CustomClientRegistrationRepo customClientRegistrationRepo;
    private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 세션 사용하지 않음
                // API 호출은 세션을 무상태성으로 관리하기 때문에 disable 가능합니다.

//                .sessionManagement((auth) -> auth
//                        .maximumSessions(1)
//                        .maxSessionsPreventsLogin(true)) // 초과시 새로운 로그인 차단
//
//                .sessionManagement((auth) -> auth
//                        .sessionFixation((sessionFixation) -> sessionFixation.changeSessionId())) // 로그인 시 세션 id 새로 생성

//                .formLogin((auth) -> auth.loginPage("/login")
//                        .loginProcessingUrl("/login").permitAll()) // 기본 로그인 활성화

//                .httpBasic(Customizer.withDefaults()) // http 헤더에 시큐리티 값을 넣음


                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다.
                .cors(AbstractHttpConfigurer::disable) // cors 비활성화 -> 프론트와 연결 시 따로 설정 필요함

                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화 -> UserNamePasswordAuthentication Fileter를 커스텀해야함
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository()) // yml 파일에 두지 않고 커스텀 형태로 만듬 + 인메모리 형식으로 저장
                        .authorizedClientService(customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, customClientRegistrationRepo.clientRegistrationRepository())) // 스프링 OAuth2 클라이언트에서 사용자의 Access 토큰이나 정보를 저장하는 OAuth2AuthorizedClientService를 DB 방식으로 구현하는 방법과 중요한 참고 사항
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig.userService(customOauth2UserService)) // 유저 처리
                        .permitAll()
                )

                // request 인증,인가 설정
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/signUp", "/reissue").permitAll()
                                .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
                                .requestMatchers("**exception**").permitAll()
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )

                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManagerBean(authenticationConfiguration), jwtUtil, authenticationSuccessHandler, authenticationFailureHandler, refreshRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);
        // UserNameAuthentication 필터를 대치해서 사용함


//                // jwt 관련 설정
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
//
//                // 인증 예외 핸들링
//                .exceptionHandling((exceptionHandling) ->
//                        exceptionHandling
//                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//                                .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return httpSecurity.build();
    }

    // 여기다가 UserDetailsService를 작성하면 인메모리 방식으로 시큐리티를 구성할 수 있음

    // 계층 권한
    // 권한이 많이질 때 개행을 하고 표시한다.
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
                """
                        ROLE_ADMIN > ROLE_CLIENT"""
        );
    }

    /**
     * Security를 적용하지 않을 리소스
     * error endpoint를 열여줘야 함
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }
}
