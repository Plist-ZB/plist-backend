package com.zerobase.plistbackend.module.websocket.service;

import static com.zerobase.plistbackend.module.channel.type.ChannelStatus.CHANNEL_STATUS_ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.repository.ChannelRepository;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.OAuth2UserException;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebSocketServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;


  @InjectMocks
  private WebSocketServiceImpl chatService;

  @Test
  @DisplayName("유저는 채팅을 보낼 수 있다")
  void success_sendMessage() {
    // given
    String sender = "testSender";
    String userImage = "TestImg.img";

    ChatMessageRequest request = ChatMessageRequest.builder()
        .sender(sender)
        .message("test message")
        .build();

    User mockUser = User.builder()
        .userName(sender)
        .userImage(userImage)
        .build();
    // when
    when(userRepository.findByUserName(sender)).thenReturn(Optional.of(mockUser));
    ChatMessageResponse response = chatService.sendMessage(request);

    assertThat(response).isNotNull();
    assertThat(response.getSender()).isEqualTo(sender);
    assertThat(response.getUserProfileImg()).isEqualTo(userImage);
  }

  @Test
  @DisplayName("유저가 아닌 회원이 메세지를 보내면 Exception이 발생한다")
  void fail_sendMessage() {
    // given
    String sender = "NotUser";
    ChatMessageRequest request = ChatMessageRequest.builder()
        .sender(sender)
        .message("test message")
        .build();

    // when
    when(userRepository.findByUserName(sender)).thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> chatService.sendMessage(request))
        .isInstanceOf(OAuth2UserException.class)
        .hasMessageContaining(  "해당 유저는 존재하지 않는 유저입니다.");
  }

  @Test
  @DisplayName("비디오 컨트롤을 요청한 유저가 호스트일 경우 true를 반환한다")
  void success_isHost() {
    // given
    Long channelId = 1L;

    User mockUser = User.builder()
        .userId(1L)
        .userName("testUser")
        .build();

    CustomOAuth2User user = mock(CustomOAuth2User.class);

    Channel mockChannel = Channel.builder()
        .channelId(1L)
        .channelHostId(mockUser.getUserId())
        .build();

    when(user.getName()).thenReturn("testUser");
    when(channelRepository.findByChannelIdAndChannelStatus(channelId,
        CHANNEL_STATUS_ACTIVE)).thenReturn(Optional.of(mockChannel));
    when(userRepository.findByUserName(mockUser.getUserName()))
        .thenReturn(Optional.of(mockUser));

    // when
    boolean result = chatService.isHost(channelId, user);

    //then
    assertThat(result).isTrue();
  }
  @Test
  @DisplayName("비디오 컨트롤을 요청한 유저가 호스트가 아닐 경우 false를 반환한다")
  void fail_isHost() {
    // given
    Long channelId = 1L;

    User mockUser = User.builder()
        .userName("testUser")
        .build();

    CustomOAuth2User user = mock(CustomOAuth2User.class);

    Channel mockChannel = Channel.builder()
        .channelId(1L)
        .channelHostId(0L)
        .build();

    when(user.getName()).thenReturn("testUser");
    when(channelRepository.findByChannelIdAndChannelStatus(channelId,
        CHANNEL_STATUS_ACTIVE)).thenReturn(Optional.of(mockChannel));
    when(userRepository.findByUserName(mockUser.getUserName()))
        .thenReturn(Optional.of(mockUser));

    // when
    boolean result = chatService.isHost(channelId, user);

    //then
    assertThat(result).isFalse();
  }
}