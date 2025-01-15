package com.zerobase.plistbackend.module.websocket.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.websocket.service.mock.TestChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestChatServiceTest {

  private final ChatService chatService = new TestChatService();

  @Test
  @DisplayName("채팅 메시지를 보낼 수 있다")
  void testSendMessage() {
    //given
    ChatMessageRequest request = ChatMessageRequest.builder()
        .sender("TestMember1")
        .message("안녕하세요")
        .build();

    ChatMessageResponse response = ChatMessageResponse
        .from(request, "testImg.testImg");

    //when
    ChatMessageResponse result = chatService.sendMessage(request);

    //then
    assertThat(result).isNotNull();
    assertThat(response.getSender()).isEqualTo("TestMember1");
    assertThat(response.getMessage()).isEqualTo("안녕하세요");
  }
  // TODO isHost
}