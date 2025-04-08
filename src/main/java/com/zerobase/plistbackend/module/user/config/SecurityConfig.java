package com.zerobase.plistbackend.module.user.config;

import com.zerobase.plistbackend.module.refresh.repository.RefreshRepository;
import com.zerobase.plistbackend.module.user.jwt.CustomLogoutFilter;
import com.zerobase.plistbackend.module.user.jwt.JwtFilter;
import com.zerobase.plistbackend.module.user.jwt.JwtUtil;
import com.zerobase.plistbackend.module.user.oauth2.CustomSuccessHandler;
import com.zerobase.plistbackend.module.user.service.CustomOAuth2UserService;
import com.zerobase.plistbackend.module.user.util.JsonResponseUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomSuccessHandler customSuccessHandler;
  private final JwtUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  private static final String[] PUBLIC_URLS = {
      "/v3/api/",
      "/oauth2/**",
      "/auth/access",
      "/", "index.html",
      "/ws-connect/**",
      "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/error",
      "/sc","/env", "/actuator/**"
  };

  private static final List<String> PUBLIC_GET_URLS = List.of(
      "/v3/api/categories",
      "/v3/api/channels",
      "/v3/api/channels/popular",
      "/v3/api/channels/search",
      "/v3/api/channels/category/**",
      "/v3/api/user/profile/*"
  );

  private static final List<String> CORS_URLS = List.of(new String[]{
      "http://ec2-13-209-237-110.ap-northeast-2.compute.amazonaws.com",
      "http://localhost:3000",
      "http://localhost:8080",
      "https://www.plist.shop",
      "https://plist.shop",
      "https://api.plist.shop"
  });


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.setAllowedOrigins(CORS_URLS);
          configuration.setAllowedMethods(
              Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
          configuration.setAllowCredentials(true);  // 쿠키 전송 허용
          configuration.setAllowedHeaders(Collections.singletonList("*"));
          configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
//          configuration.setMaxAge(3600L);
          return configuration;
        }));

    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable);

    // 경로별 인가 작업
    http.authorizeHttpRequests((auth) -> {
      for (String url : PUBLIC_GET_URLS) {
        auth.requestMatchers(HttpMethod.GET, url).permitAll();
      }
      auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
      auth.requestMatchers(PUBLIC_URLS).permitAll();
      auth.anyRequest().authenticated();
    });

    // oauth2
    http
        .oauth2Login((oauth2) ->
            oauth2
                .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuth2UserService)))
                .successHandler(customSuccessHandler)
        );

    // 인증되지 않은 사용자에게 401 Unauthorized 반환
    http
        .exceptionHandling((exception) ->
            exception.authenticationEntryPoint((request, response, authException) ->
                JsonResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED,
                    "Unauthorized")));

    // JwtFilter 추가
    http
        .addFilterBefore(new JwtFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

    http
        .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository),
            LogoutFilter.class);

    // 세션 설정 : STATELESS
    http
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
