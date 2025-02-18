package project.studyproject.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security에 대한 디버깅 모드를 사용하기 위한 어노테이션 (default : false)
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/v1/user/signIn", "/api/v1/user/signUp").permitAll()
                                .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                                .requestMatchers("**exception**").permitAll()
                                .anyRequest().hasRole("ADMIN"))

                .formLogin(AbstractHttpConfigurer::disable)

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                                .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return httpSecurity.build();
    }
}
