package com.zerobase.plistbackend.module.chatting.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.chatting.domain.VideoSyncManager;
import com.zerobase.plistbackend.module.chatting.dto.request.VideoSyncRequest;
import com.zerobase.plistbackend.module.chatting.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.chatting.dto.request.VideoSyncResponse;
import com.zerobase.plistbackend.module.chatting.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.chatting.exception.ChatException;
import com.zerobase.plistbackend.module.chatting.service.ChatService;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

  @Mock
  private ChatService chatService;

  @Mock
  private VideoSyncManager manager;

  @InjectMocks
  private ChatController chatController;

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
    when(chatService.sendMessage(request)).thenReturn(response);
    ChatMessageResponse messageResponse = chatController.sendMessage(request);

    //then
    assertThat(response).isEqualTo(messageResponse);
    assertThat(response.getSender()).isEqualTo("TestMember1");
    assertThat(response.getMessage()).isEqualTo("안녕하세요");
  }

  @Test
  @DisplayName("비디오의 currentTime을 업데이트 한다")
  void testSyncVideo() {
    //given
    VideoSyncRequest request = VideoSyncRequest.builder()
        .videoId("TestVideoId")
        .playState(1L)
        .currentTime(200L)
        .build();

    Long channelId = 1L;

    //when
    VideoSyncResponse response = chatController.syncVideo(channelId, request);

    //then
    assertThat(request.getCurrentTime()).isEqualTo(response.getCurrentTime());
  }

  @Test
  @DisplayName("채널의 호스트는 비디오의 재생 및 일시정지를 누를 수 있다")
  void testControlVideo() {
    //given
    VideoSyncRequest request = VideoSyncRequest.builder()
        .videoId("TestVideoId")
        .playState(1L)
        .currentTime(200L)
        .build();
    Long channelId = 1L;

    CustomOAuth2User user = mock(CustomOAuth2User.class);

    //when
    when(chatService.isHost(user)).thenReturn(true);
    VideoSyncResponse response = chatController.controlVideo(channelId, request, user);

    //then
    assertThat(response.getCurrentTime()).isEqualTo(response.getCurrentTime());
  }

  @Test
  @DisplayName("채널의 호스트가 아닐 경우 재생 및 일시정지를 누르면 에러가 발생한다")
  void testControlVideoNotHost() {
    //given
    VideoSyncRequest request = VideoSyncRequest.builder()
        .videoId("TestVideoId")
        .playState(1L)
        .currentTime(200L)
        .build();
    Long channelId = 1L;

    CustomOAuth2User user = mock(CustomOAuth2User.class);

    //when
    when(chatService.isHost(user)).thenReturn(false);

    //then
    ChatException exception = assertThrows(ChatException.class, () ->
        chatController.controlVideo(channelId, request, user));

    assertThat(exception.getMessage())
        .isEqualTo("해당 권한은 호스트만 가능합니다.");
    assertThat(exception.getErrorCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(exception.getErrorType()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
  }

  @Test
  @DisplayName("채널에 새롭게 들어온 유저는 현재 영상의 currentTime을 받아 영상 시점이 호스트와 같게 동기화 된다")
  void testSyncVideoForNewUser() {
      //given
    VideoSyncRequest request = VideoSyncRequest.builder()
        .videoId("TestVideoId")
        .playState(1L)
        .currentTime(200L)
        .build();
    Long channelId = 1L;

    //when
    VideoSyncResponse response = chatController.syncVideoForNewUser(channelId, request);
    //the
    assertThat(response.getCurrentTime()).isEqualTo(response.getCurrentTime());
  }
}