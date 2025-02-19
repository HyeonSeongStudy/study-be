package project.studyproject.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security에 대한 디버깅 모드를 사용하기 위한 어노테이션 (default : false)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
//    private final OAuth2SuccessHandler oAuth2SuccessHandler;
//    private final CustomOAuth2UserService oAuth2UserService;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다.
                .cors(AbstractHttpConfigurer::disable) // cors 비활성화 -> 프론트와 연결 시 따로 설정 필요함

                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 세션 사용하지 않음
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                // .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/login").permitAll()) // 기본 로그인 활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 비활성화

                // request 인증,인가 설정
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/v1/user/signIn", "/api/v1/user/signUp").permitAll()
                                .requestMatchers("**exception**").permitAll()
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().authenticated()
                );

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
}
