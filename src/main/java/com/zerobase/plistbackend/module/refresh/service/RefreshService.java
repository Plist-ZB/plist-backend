package com.zerobase.plistbackend.module.refresh.service;

import com.zerobase.plistbackend.module.refresh.dto.NewAccessResponse;
import com.zerobase.plistbackend.module.refresh.entity.Refresh;
import com.zerobase.plistbackend.module.refresh.repository.RefreshRepository;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.jwt.JwtUtil;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RefreshService {

  private final Long refreshExpired;
  private final RefreshRepository refreshRepository;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public RefreshService(@Value("${jwt.refresh}") Long refreshExpired,
      RefreshRepository refreshRepository,
      JwtUtil jwtUtil,
      UserRepository userRepository) {
    this.refreshExpired = refreshExpired;
    this.refreshRepository = refreshRepository;
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
  }

  @Transactional
  public void addRefreshEntity(String email, String token, Timestamp expired) {

    Optional<Refresh> existingRefresh = refreshRepository.findByUser_UserEmail(email);

    if (existingRefresh.isPresent()) {
      Refresh refresh = existingRefresh.get();
      refresh.updateRefreshToken(token, expired);
      log.info("Updated Refresh token: {}", refresh.getRefreshToken());
    } else {
      Refresh refresh = Refresh.builder()
          .user(userRepository.findByUserEmail(email))
          .refreshToken(token)
          .refreshExpiration(expired)
          .build();
      refreshRepository.save(refresh);
      log.info("Created new Refresh token: {}", refresh.getRefreshToken());
    }
  }

  public void checkRefresh(String refreshToken) {

    log.info("Check refresh token: {}", refreshToken);

    // refresh 토큰이 없는 경우
    if (refreshToken == null) {
      log.error("refresh token null");
    }

    // refresh 토큰이 만료가 된 경우
    try {
      jwtUtil.isExpired(refreshToken);
    } catch (ExpiredJwtException e) {
      log.error("refresh token expired");
    }

    // 토큰이 refresh 인지 확인
    String category = jwtUtil.findCategory(refreshToken);
    if (!category.equals("refresh")) {
      log.error("refresh token invalid");
    }
  }

  public NewAccessResponse newAccessToken(HttpServletRequest request) {
    String refreshToken = jwtUtil.findToken(request, "refresh");
    checkRefresh(refreshToken);

    String email = jwtUtil.findEmail(refreshToken);
    String role = jwtUtil.findRole(refreshToken);

    return new NewAccessResponse(jwtUtil.createJwt("access", email, role));
  }
}
