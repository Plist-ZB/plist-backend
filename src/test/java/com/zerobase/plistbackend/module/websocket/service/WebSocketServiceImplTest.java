package com.zerobase.plistbackend.module.websocket.service;

import static com.zerobase.plistbackend.module.channel.type.ChannelStatus.CHANNEL_STATUS_ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.repository.ChannelRepository;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.OAuth2UserException;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(MockitoExtension.class)
@Transactional
class WebSocketServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;


  @InjectMocks
  private WebSocketServiceImpl webSocketService;

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
    ChatMessageResponse response = webSocketService.sendMessage(request);

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
    assertThatThrownBy(() -> webSocketService.sendMessage(request))
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
        .build();

    userRepository.save(mockUser);
    Channel mockChannel = Channel.builder()
        .channelId(1L)
        .channelParticipants(List.of(Participant.builder()
            .participantId(mockUser.getUserId())
            .isHost(true)
            .user(mockUser)
            .build()))
        .channelHostId(mockUser.getUserId())
        .build();


    channelRepository.save(mockChannel);

    when(channelRepository.findByChannelIdAndChannelStatus(channelId,
        CHANNEL_STATUS_ACTIVE)).thenReturn(Optional.of(mockChannel));

    boolean isHost = mockChannel.getChannelParticipants().stream()
        .anyMatch(Participant::getIsHost);
    System.out.println("isHost = " + isHost);

    // when
    boolean result = webSocketService.isHost(channelId);
    System.out.println("result = " + result);

    //then
    assertThat(result).isTrue();
    assertThat(isHost).isTrue();
  }
  @Test
  @DisplayName("비디오 컨트롤을 요청한 유저가 호스트가 아닐 경우 false를 반환한다")
  void fail_isHost() {
    // given
    Long channelId = 1L;

    Channel mockChannel = Channel.builder()
        .channelId(1L)
        .channelHostId(0L)
        .build();

    channelRepository.save(mockChannel);

    when(channelRepository.findByChannelIdAndChannelStatus(channelId,
        CHANNEL_STATUS_ACTIVE)).thenReturn(Optional.of(mockChannel));

    boolean isHost = mockChannel.getChannelParticipants().stream()
        .anyMatch(Participant::getIsHost);

    // when
    boolean result = webSocketService.isHost(channelId);

    //then
    assertThat(result).isFalse();
    assertThat(isHost).isFalse();
  }
}