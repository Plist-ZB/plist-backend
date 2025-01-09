package com.zerobase.plistbackend.module.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  private User mockUser;

  @BeforeEach
  void setUp() {
    mockUser = User.builder()
        .userEmail("test@example.com")
        .userName("Test User")
        .userImage("test-image-url")
        .userRole(UserRole.ROLE_USER)
        .build();
  }

  @Test
  @DisplayName("유저 정보 찾기 성공")
  void testFindProfile_Success() {
    String email = "test@example.com";
    when(userRepository.findByUserEmail(email)).thenReturn(mockUser);

    ProfileResponse response = userService.findProfile(email);

    assertEquals(email, response.email());
    assertEquals("Test User", response.nickname());
    assertEquals("test-image-url", response.image());
    
    verify(userRepository).findByUserEmail(email);
  }
}
