package com.zerobase.plistbackend.module.refresh.service;

import com.zerobase.plistbackend.module.refresh.dto.NewAccessResponse;
import com.zerobase.plistbackend.module.refresh.exception.RefreshException;
import com.zerobase.plistbackend.module.refresh.repository.RefreshRepository;
import com.zerobase.plistbackend.module.refresh.type.RefreshErrorStatus;
import com.zerobase.plistbackend.module.user.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

  private final RefreshRepository refreshRepository;
  private final JwtUtil jwtUtil;

  public void addRefreshEntity(Long userId, String token) {
    refreshRepository.saveToken(userId, token);
    log.info("Created new Refresh token: {}", token);
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

}
