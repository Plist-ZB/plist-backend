package com.zerobase.plistbackend.module.user.jwt;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.model.auth.UserDetail;
import com.zerobase.plistbackend.module.user.type.UserRole;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
      "/v3/api/categories", "/v3/api/channels", "/v3/api/channels/popular", "/v3/api/channels/search");

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

    if(GET_ALLOWED_PATH.stream().anyMatch(path::equals) && request.getMethod().equals("GET")
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
//      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//      response.addHeader("message", "error");
//      filterChain.doFilter(request, response);
//      return;
//    }

    try {
      jwtUtil.isExpired(accessToken);
    } catch (ExpiredJwtException e) {
      log.error("access token expired");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      filterChain.doFilter(request, response);
      return;
    }

    // access token 인지 확인
    String category = jwtUtil.findCategory(accessToken);
    if (!category.equals("access")) {
      PrintWriter writer = response.getWriter();
      writer.print("invalid token");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      return;
    }

    UserRole userRole = UserRole.valueOf(jwtUtil.findRole(accessToken));
    if (userRole == UserRole.ROLE_NONE) {
      PrintWriter writer = response.getWriter();
      writer.print("invalid role");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
