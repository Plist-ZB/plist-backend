package com.zerobase.plistbackend.module.chatting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.chatting.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.chatting.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.OAuth2UserException;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ChatServiceImpl chatService;

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
  void isHost_ShouldReturnFalse_WhenCalled() {
    // TODO -> HOST를 식별할 무언가가 필요
    // Act
    boolean result = chatService.isHost(null);

    // Assert
    assertThat(result).isFalse();
  }
}