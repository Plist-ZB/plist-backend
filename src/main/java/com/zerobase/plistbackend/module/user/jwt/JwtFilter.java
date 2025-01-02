package com.zerobase.plistbackend.module.user.jwt;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.model.auth.UserDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authorization = null;
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("Authorization")) {
        authorization = cookie.getValue();
      }
    }

    if (authorization == null) {
      log.warn("Authorization cookie not found");
      filterChain.doFilter(request, response);
      return;
    }

    if (jwtUtil.isExpired(authorization)) {
      log.warn("Authorization expired");
      filterChain.doFilter(request, response);
      return;
    }

    String email = jwtUtil.findEmail(authorization);
    String role = jwtUtil.findRole(authorization);

    UserDetail userDetail = UserDetail.builder().email(email).role(role).build();
    CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDetail);

    Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }
}
