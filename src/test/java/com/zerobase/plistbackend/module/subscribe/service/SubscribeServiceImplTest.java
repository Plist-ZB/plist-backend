package com.zerobase.plistbackend.module.subscribe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.subscribe.dto.response.SubscribeResponse;
import com.zerobase.plistbackend.module.subscribe.entity.Subscribe;
import com.zerobase.plistbackend.module.subscribe.repository.SubscribeRepository;
import com.zerobase.plistbackend.module.subscribe.dto.response.FolloweeInfoResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscribeServiceImplTest {

  @InjectMocks
  private SubscribeServiceImpl subscribeService;

  @Mock
  private SubscribeRepository subscribeRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CustomOAuth2User customOAuth2User;

  private User mockFollower;
  private User mockFollowee;
  private final String email = "test@test.com";

  @BeforeEach
  void setUp() {
    mockFollower = User.builder()
        .userId(1L)
        .userEmail(email)
        .build();

    when(customOAuth2User.findEmail()).thenReturn(email);
    when(userRepository.findByUserEmail(email)).thenReturn(mockFollower);
  }

  @Test
  @DisplayName("사용자의 구독리스트를 전체 조회합니다.")
  void findFollowees() {

    FolloweeInfoResponse followee1 = new FolloweeInfoResponse(2L, "테스터1");
    FolloweeInfoResponse followee2 = new FolloweeInfoResponse(3L, "테스터2");
    List<FolloweeInfoResponse> followees = Arrays.asList(followee1, followee2);

    when(subscribeRepository.findByFollower(mockFollower)).thenReturn(followees);

    SubscribeResponse response = subscribeService.findFollowees(customOAuth2User);

    assertThat(response.getFollowees()).hasSize(2);
  }

  @Test
  @DisplayName("사용자는 한 사용자를 구독할 수 있습니다.")
  void subcribe() {

    mockFollowee = User.builder()
        .userId(2L)
        .build();

    when(userRepository.findByUserId(mockFollowee.getUserId())).thenReturn(
        Optional.ofNullable(mockFollowee));

    subscribeService.subcribe(customOAuth2User, mockFollowee.getUserId());

    verify(subscribeRepository, times(1)).save(any(Subscribe.class));
  }

  @Test
  @DisplayName("사용자는 구독한 사용자에 대해 구독취소할 수 있습니다.")
  void unsubscribe() {

    Subscribe subscribe = mock(Subscribe.class);

    mockFollowee = User.builder()
        .userId(2L)
        .build();

    when(userRepository.findByUserId(mockFollowee.getUserId())).thenReturn(
        Optional.ofNullable(mockFollowee));

    when(subscribeRepository.findByFollowerAndFollowee(mockFollower, mockFollowee)).thenReturn(
        Optional.ofNullable(subscribe));

    subscribeService.unsubscribe(customOAuth2User, 2L);

    verify(subscribeRepository, times(1))
        .delete(Objects.requireNonNull(subscribe));
  }
}