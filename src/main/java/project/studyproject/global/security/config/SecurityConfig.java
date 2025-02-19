package project.studyproject.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import project.studyproject.global.security.jwt.JWTFilter;
import project.studyproject.global.security.jwt.JWTUtil;
import project.studyproject.global.security.jwt.LoginFilter;
import project.studyproject.global.security.JwtTokenProvider;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security에 대한 디버깅 모드를 사용하기 위한 어노테이션 (default : false)
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;

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
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화 -> UserNamePasswordAuthentication Fileter를 커스텀해야함
                .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 비활성화

                // request 인증,인가 설정
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/v1/user/signIn", "/api/v1/user/signUp").permitAll()
                                .requestMatchers("**exception**").permitAll()
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )

                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManagerBean(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
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
