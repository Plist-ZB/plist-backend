package com.zerobase.plistbackend.module.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.refresh.dto.NewAccessResponse;
import com.zerobase.plistbackend.module.refresh.entity.Refresh;
import com.zerobase.plistbackend.module.refresh.exception.RefreshException;
import com.zerobase.plistbackend.module.refresh.repository.RefreshRepository;
import com.zerobase.plistbackend.module.refresh.service.RefreshServiceImpl;
import com.zerobase.plistbackend.module.refresh.type.RefreshErrorStatus;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.jwt.JwtUtil;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RefreshServiceTest {

  private RefreshRepository refreshRepository;
  private UserRepository userRepository;
  private JwtUtil jwtUtil;
  private RefreshServiceImpl refreshService;

  @BeforeEach
  void setUp() {
    refreshRepository = Mockito.mock(RefreshRepository.class);
    userRepository = Mockito.mock(UserRepository.class);
    jwtUtil = Mockito.mock(JwtUtil.class);
    refreshService = new RefreshServiceImpl(refreshRepository, userRepository, jwtUtil);
  }

  @Test
  @DisplayName("리프레시 토큰 발행 - 성공")
  void addRefreshEntity_ShouldUpdateExistingRefresh() {
    String email = "test@example.com";
    String token = "newRefreshToken";
    Timestamp expired = new Timestamp(System.currentTimeMillis() + 10000);

    User mockUser = User.builder()
        .userId(1L)
        .userEmail(email)
        .userName("Test User")
        .userImage("test-image-url")
        .userRole(UserRole.ROLE_USER)
        .build();

    Refresh existingRefresh = Refresh.builder()
        .user(mockUser)
        .refreshToken(token)
        .refreshExpiration(expired)
        .build();
    existingRefresh.updateRefreshToken("oldToken", expired);

    when(refreshRepository.findByUser_UserEmail(email)).thenReturn(Optional.of(existingRefresh));

    refreshService.addRefreshEntity(email, token, expired);

    assertEquals(token, existingRefresh.getRefreshToken());
  }

  @Test
  @DisplayName("리프레시 토큰이 없음 - 오류 확인")
  void checkRefresh_ShouldThrowExceptionWhenTokenIsNull() {
    RefreshException exception = assertThrows(RefreshException.class,
        () -> refreshService.checkRefresh(null));
    assertEquals(RefreshErrorStatus.REFRESH_NOT_FOUND, exception.getErrorStatus());
  }

  @Test
  @DisplayName("리프레시 토큰 만료 - 오류 확인")
  void checkRefresh_ShouldThrowExceptionWhenTokenIsExpired() {
    String refreshToken = "expiredToken";
    doThrow(new RefreshException(RefreshErrorStatus.REFRESH_EXPIRED)).when(jwtUtil)
        .isExpired(refreshToken);

    RefreshException exception = assertThrows(RefreshException.class,
        () -> refreshService.checkRefresh(refreshToken));
    assertEquals(RefreshErrorStatus.REFRESH_EXPIRED, exception.getErrorStatus());
  }

  @Test
  @DisplayName("액세스 토큰 재발행 - 성공")
  void newAccessToken_ShouldReturnNewAccessResponse() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    String refreshToken = "validToken";
    String email = "test@example.com";
    String role = "USER";
    String accessToken = "newAccessToken";

    when(jwtUtil.findToken(request, "refresh")).thenReturn(refreshToken);
    when(jwtUtil.isExpired(refreshToken)).thenReturn(false);
    when(jwtUtil.findEmail(refreshToken)).thenReturn(email);
    when(jwtUtil.findRole(refreshToken)).thenReturn(role);
    when(jwtUtil.findCategory(refreshToken)).thenReturn("refresh");
    when(jwtUtil.createJwt("access", email, role)).thenReturn(accessToken);

    NewAccessResponse response = refreshService.newAccessToken(request);

    assertEquals(accessToken, response.accessToken());
  }
}
