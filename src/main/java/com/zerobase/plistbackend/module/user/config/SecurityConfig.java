package com.zerobase.plistbackend.module.user.config;

import com.zerobase.plistbackend.module.user.jwt.JwtFilter;
import com.zerobase.plistbackend.module.user.jwt.JwtUtil;
import com.zerobase.plistbackend.module.user.oauth2.CustomSuccessHandler;
import com.zerobase.plistbackend.module.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomSuccessHandler customSuccessHandler;
  private final JwtUtil jwtUtil;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // csrf disable
    http
        .csrf(AbstractHttpConfigurer::disable);

    // Form 로그인 방식 disable
    http
        .formLogin(AbstractHttpConfigurer::disable);

    // HTTP Basic 인증 방식 disable
    http
        .httpBasic(AbstractHttpConfigurer::disable);

    // JwtFilter 추가
    http
        .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

    // oauth2
    http
        .oauth2Login((oauth2) ->
            oauth2
                .loginPage("/oauth2/authorization/google")
                .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                .userService(customOAuth2UserService)))
                .successHandler(customSuccessHandler)
        );


    // 경로별 인가 작업
    http
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers("/", "/login", "/oauth2/**").permitAll()
            .anyRequest().permitAll());
//            .anyRequest().authenticated());

    // 세션 설정 : STATELESS
    http
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
