package com.zerobase.plistbackend.module.user.oauth2;

import com.zerobase.plistbackend.module.refresh.service.RefreshService;
import com.zerobase.plistbackend.module.user.jwt.JwtUtil;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtUtil jwtUtil;
  private final RefreshService refreshService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    String email = customOAuth2User.findEmail();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = auth.getAuthority();

    String access = jwtUtil.createJwt("access", email, role);
    String refresh = jwtUtil.createJwt("refresh", email, role);
    Timestamp expired = jwtUtil.findExpiration(refresh);
    refreshService.addRefreshEntity(email, refresh, expired);

    response.setStatus(HttpServletResponse.SC_FOUND);
    ResponseCookie cookie = jwtUtil.createCookie("refresh", refresh);
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

//    String url = String.format("https://plist-veta96s-projects.vercel.app/auth/redirect?access-token=%s&is-member=%s",
    String url = String.format("http://localhost:3000/auth/redirect?access-token=%s&is-member=%s",
//    String url = String.format("http://localhost:8080/",
        access, customOAuth2User.findIsMember());
    response.sendRedirect(url);
  }


}
