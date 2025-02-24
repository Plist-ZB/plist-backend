package com.zerobase.plistbackend.module.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.user.dto.request.UserProfileRequest;
import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.UserErrorStatus;
import com.zerobase.plistbackend.module.user.type.UserRole;
import com.zerobase.plistbackend.module.user.util.S3Util;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  private CustomOAuth2User mockOAuth2User;

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private User mockUser;

  @Mock
  private S3Util s3Util;

  @BeforeEach
  void setUp() {
    mockUser = User.builder()
        .userId(1L)
        .userEmail("test@example.com")
        .userName("Test User")
        .userImage("test-image-url")
        .userRole(UserRole.ROLE_USER)
        .build();

    mockOAuth2User = mock(CustomOAuth2User.class);
    lenient().doReturn(1L).when(mockOAuth2User).findId();
  }

  @Test
  @DisplayName("유저 정보 찾기 성공")
  void testFindProfileSuccess() {
    String email = "test@example.com";
    ProfileResponse response = new ProfileResponse("test@example.com", "Test User",
        "test-image-url");
    when(userRepository.findProfileByEmail(email)).thenReturn(response);

    ProfileResponse actualResponse = userService.findProfile(email);

    assertEquals(email, actualResponse.email());
    assertEquals("Test User", actualResponse.nickname());
    assertEquals("test-image-url", actualResponse.image());

    verify(userRepository).findProfileByEmail(email);
  }

  @Test
  @DisplayName("회원 정보 수정 성공 - 닉네임과 이미지 변경")
  void testEditProfileWithMultipartFile() {
    // Given
    Long userId = 1L;
    String newNickname = "Updated User";

    MockMultipartFile imageFile = new MockMultipartFile(
        "image", "test.jpg", "image/jpeg", new byte[]{1, 2, 3, 4}
    );

    UserProfileRequest request = new UserProfileRequest(imageFile, newNickname);

    when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
    when(s3Util.putImage(imageFile, mockUser.getUserEmail())).thenReturn("updated-image-url");

    // When
    ProfileResponse response = userService.editProfile(request, userId);

    // Then
    assertEquals("Updated User", response.nickname());
    assertEquals("updated-image-url", response.image());
  }

  @Test
  @DisplayName("회원 탈퇴 테스트")
  void userDrawTest() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

    userService.withdrawUser(1L);

    assertEquals(UserRole.ROLE_NONE, mockUser.getUserRole());
    verify(userRepository).findById(1L);
  }

  @Test
  @DisplayName("회원 탈퇴 실패 - 유저 없음")
  void testWithdrawUserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    UserException exception = assertThrows(UserException.class, () -> userService.withdrawUser(1L));

    assertEquals(UserErrorStatus.USER_NOT_FOUND, exception.getErrorStatus());
    verify(userRepository).findById(1L);
  }
}
