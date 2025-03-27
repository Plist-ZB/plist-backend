package com.zerobase.plistbackend.module.fcmtoken.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.fcmtoken.entity.FCMToken;
import com.zerobase.plistbackend.module.fcmtoken.repository.FCMTokenRepository;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // Mockito 확장 기능 활성화
class FCMTokenServiceImplTest {

  @InjectMocks
  private FCMTokenServiceImpl fcmTokenService;

  @Mock
  private FCMTokenRepository fcmTokenRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CustomOAuth2User customOAuth2User;

  private User mockUser;

  private final String email = "test@test.com";
  private final String token = "test_token";

  @BeforeEach
  void setUp() {
    mockUser = User.builder()
        .userId(1L)
        .userEmail(email)
        .build();
  }

  @Test
  @DisplayName("사용자의 FCMToken이 존재하지 않는 경우에는 저장합니다.")
  void saveFCMToken() {
    // Given
    when(customOAuth2User.findEmail()).thenReturn(email);
    when(userRepository.findByUserEmail(email)).thenReturn(mockUser);
    when(fcmTokenRepository.findByFcmTokenValue(token)).thenReturn(Optional.empty());

    // When
    fcmTokenService.upsertFCMToken(customOAuth2User, token);

    // Then
    verify(fcmTokenRepository, times(1)).save(any(FCMToken.class));
  }

  @Test
  @DisplayName("사용자의 FCMToken이 존재하는 경우에는 생성시간을 업데이트합니다.")
  void updateFCMToken() {
    // Given
    FCMToken existsToken = mock(FCMToken.class);
    when(customOAuth2User.findEmail()).thenReturn(email);
    when(userRepository.findByUserEmail(email)).thenReturn(mockUser);
    when(fcmTokenRepository.findByFcmTokenValue(token)).thenReturn(Optional.of(existsToken));

    // When
    fcmTokenService.upsertFCMToken(customOAuth2User, token);

    // Then
    verify(existsToken, times(1)).updateFCMToken();
    verify(fcmTokenRepository, times(1)).save(existsToken);
  }
}