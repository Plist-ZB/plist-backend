package com.zerobase.plistbackend.module.refresh.service;

import com.zerobase.plistbackend.module.refresh.dto.NewAccessResponse;
import com.zerobase.plistbackend.module.refresh.entity.Refresh;
import com.zerobase.plistbackend.module.refresh.exception.RefreshException;
import com.zerobase.plistbackend.module.refresh.repository.RefreshRepository;
import com.zerobase.plistbackend.module.refresh.type.RefreshErrorStatus;
import com.zerobase.plistbackend.module.user.jwt.JwtUtil;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

  private final RefreshRepository refreshRepository;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

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

    if (refreshToken == null) {
      throw new RefreshException(RefreshErrorStatus.REFRESH_NOT_FOUND);
    }

    try {
      jwtUtil.isExpired(refreshToken);
    } catch (ExpiredJwtException e) {
      throw new RefreshException(RefreshErrorStatus.REFRESH_EXPIRED);
    }

    // 토큰이 refresh 인지 확인
    String category = jwtUtil.findCategory(refreshToken);
    if (!category.equals("refresh")) {
      throw new RefreshException(RefreshErrorStatus.REFRESH_INVALID);
    }
  }

  public NewAccessResponse newAccessToken(HttpServletRequest request) {
    String refreshToken = jwtUtil.findToken(request, "refresh");
    checkRefresh(refreshToken);

    String email = jwtUtil.findEmail(refreshToken);
    String role = jwtUtil.findRole(refreshToken);

    return new NewAccessResponse(jwtUtil.createJwt("access", email, role));
  }

  @Transactional
  @Scheduled(cron = "0 0 0 * * *")
  public void refreshCleanup() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    refreshRepository.deleteByRefreshExpirationBefore(now);
  }
}
