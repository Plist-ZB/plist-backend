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

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String accessToken = request.getHeader("Authorization");
    if (accessToken == null || !accessToken.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    accessToken = accessToken.substring("Bearer ".length());

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
