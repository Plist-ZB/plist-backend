package com.zerobase.plistbackend.module.user.jwt;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.model.auth.UserDetail;
import com.zerobase.plistbackend.module.user.type.UserRole;
import com.zerobase.plistbackend.module.user.util.JsonResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  private static final Set<String> ALLOWED_PATHS = Set.of("/v3/api/", "/favicon.ico",
      "/v3/api/auth/access", "/");

  private static final Set<String> GET_ALLOWED_PATH = Set.of(
      "/v3/api/categories", "/v3/api/channels", "/v3/api/channels/popular",
      "/v3/api/channels/search");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // 특정 경로에 대한 필터 건너뛰기
    String path = request.getRequestURI();
    if (ALLOWED_PATHS.stream().anyMatch(path::equals)) {
      SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(null, null, new ArrayList<>()));
      filterChain.doFilter(request, response);
      return;
    }

    if (GET_ALLOWED_PATH.stream().anyMatch(path::equals) && request.getMethod().equals("GET")
        || path.startsWith("/v3/api/channels/category/") && request.getMethod().equals("GET")) {
      SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(null, null, new ArrayList<>()));
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = request.getHeader("Authorization");
    if (accessToken == null || !accessToken.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    accessToken = accessToken.substring("Bearer ".length());
//    String refreshToken = jwtUtil.findToken(request, "refresh");
//    log.info("access token: {}, refresh token : {}", accessToken, refreshToken);
//
//    if(refreshToken == null) {
//      log.error("refresh token is null");
//      JsonResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "refresh token is null");
//      return;
//    }

    try {
      jwtUtil.isExpired(accessToken);
    } catch (ExpiredJwtException e) {
      log.error("access token expired");
      JsonResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "access token expired");
      return;
    }

    // access token 인지 확인
    String category = jwtUtil.findCategory(accessToken);
    if (!category.equals("access")) {
      JsonResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "invalid access token");
      return;
    }

    UserRole userRole = UserRole.valueOf(jwtUtil.findRole(accessToken));
    if (userRole == UserRole.ROLE_NONE) {
      log.error("invalid token role");
      JsonResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "invalid token role");
    }

    Long id = jwtUtil.findId(accessToken);
    String email = jwtUtil.findEmail(accessToken);
    String role = jwtUtil.findRole(accessToken);

    UserDetail userDetail = UserDetail.builder().id(id).email(email).role(role).build();
    CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDetail);

    Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
        customOAuth2User.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }
}
