package com.zerobase.plistbackend.module.refresh.controller;

import com.zerobase.plistbackend.module.refresh.dto.NewAccessResponse;
import com.zerobase.plistbackend.module.refresh.service.RefreshServiceImpl;
import com.zerobase.plistbackend.module.user.jwt.JwtUtil;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api")
@Tag(name = "Jwt Token API", description = "Access token 관리")
public class RefreshController {

  private final RefreshServiceImpl refreshService;
  private final JwtUtil jwtUtil;

  @GetMapping("/auth/access")
  public ResponseEntity<NewAccessResponse> newAccess(
      HttpServletRequest request, HttpServletResponse response,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    String refresh = jwtUtil.createJwt("refresh", customOAuth2User.findEmail(),
        customOAuth2User.getAuthorities().iterator().next().getAuthority());

    NewAccessResponse newAccessToken = refreshService.newAccessToken(request, refresh);

    ResponseCookie cookie = jwtUtil.createCookie("refresh", refresh);
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.status(201).body(newAccessToken);
  }
}
