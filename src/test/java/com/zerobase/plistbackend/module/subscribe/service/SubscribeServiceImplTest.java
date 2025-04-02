package com.zerobase.plistbackend.module.subscribe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.subscribe.dto.response.FollowerInfoResponse;
import com.zerobase.plistbackend.module.subscribe.dto.response.SubscribeResponse;
import com.zerobase.plistbackend.module.subscribe.entity.Subscribe;
import com.zerobase.plistbackend.module.subscribe.repository.SubscribeRepository;
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

  private final String email = "test@test.com";
  @InjectMocks
  private SubscribeServiceImpl subscribeService;
  @Mock
  private SubscribeRepository subscribeRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private CustomOAuth2User customOAuth2User;
  private User mockFollowee;
  private User mockFollower;

  @BeforeEach
  void setUp() {
    mockFollowee = User.builder()
        .userId(1L)
        .userEmail(email)
        .build();

    when(customOAuth2User.findEmail()).thenReturn(email);
    when(userRepository.findByUserEmail(email)).thenReturn(mockFollowee);
  }

  @Test
  @DisplayName("사용자의 구독리스트를 전체 조회합니다.")
  void findFollowers() {

    FollowerInfoResponse follower1 = new FollowerInfoResponse(2L, "테스터1", "테스트이미지");
    FollowerInfoResponse follower2 = new FollowerInfoResponse(3L, "테스터2", "테스트이미지");
    List<FollowerInfoResponse> followers = Arrays.asList(follower1, follower2);

    when(subscribeRepository.findByFollowee(mockFollowee)).thenReturn(followers);

    SubscribeResponse response = subscribeService.findFollowers(customOAuth2User);

    assertThat(response.getFollowers()).hasSize(2);
  }

  @Test
  @DisplayName("사용자는 한 사용자를 구독할 수 있습니다.")
  void subscribe() {

    mockFollower = User.builder()
        .userId(2L)
        .build();

    when(userRepository.findByUserId(mockFollower.getUserId())).thenReturn(
        Optional.ofNullable(mockFollower));

    subscribeService.subscribe(customOAuth2User, mockFollower.getUserId());

    verify(subscribeRepository, times(1)).save(any(Subscribe.class));
  }

  @Test
  @DisplayName("사용자는 구독한 사용자에 대해 구독취소할 수 있습니다.")
  void unsubscribe() {

    Subscribe subscribe = mock(Subscribe.class);

    mockFollower = User.builder()
        .userId(2L)
        .build();

    when(userRepository.findByUserId(mockFollower.getUserId())).thenReturn(
        Optional.ofNullable(mockFollower));

    when(subscribeRepository.findByFolloweeAndFollower(mockFollowee, mockFollower)).thenReturn(
        Optional.ofNullable(subscribe));

    subscribeService.unsubscribe(customOAuth2User, 2L);

    verify(subscribeRepository, times(1))
        .delete(Objects.requireNonNull(subscribe));
  }
}