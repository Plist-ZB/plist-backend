package com.zerobase.plistbackend.module.user.oauth2;

import com.zerobase.plistbackend.module.user.jwt.JwtUtil;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final Long EXPIRES_IN_SECONDS = 600000L;

  private final JwtUtil jwtUtil;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

    String email = customOAuth2User.findEmail();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = auth.getAuthority();

    String token = jwtUtil.createJwt(email, role, EXPIRES_IN_SECONDS);

    response.addCookie(createCookie("Authorization", token));
    response.sendRedirect(request.getContextPath() + "/"); // 추후 변경
  }

  private Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(EXPIRES_IN_SECONDS.intValue());
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    return cookie;
  }
}
