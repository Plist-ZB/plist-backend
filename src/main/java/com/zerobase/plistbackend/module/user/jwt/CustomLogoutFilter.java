package com.zerobase.plistbackend.module.user.jwt;

import com.zerobase.plistbackend.module.refresh.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

  private final JwtUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
  }

  @Transactional
  protected void doFilter(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {

    String requestUri = request.getRequestURI();
    if (!requestUri.matches("^\\/logout$")) {
      chain.doFilter(request, response);
      return;
    }
    String requestMethod = request.getMethod();
    if (!requestMethod.equals("POST")) {
      chain.doFilter(request, response);
      return;
    }

    String refresh = jwtUtil.findToken(request, "refresh");
    if (refresh == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    String category = jwtUtil.findCategory(refresh);
    if (!category.equals("refresh")) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    Long userId = jwtUtil.findId(refresh);
    if (refreshRepository.hasToken(userId)) {
      refreshRepository.deleteByToken(userId);
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    Cookie cookie = new Cookie("refresh", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");

    response.addCookie(cookie);
    response.setStatus(HttpServletResponse.SC_OK);
  }
}

