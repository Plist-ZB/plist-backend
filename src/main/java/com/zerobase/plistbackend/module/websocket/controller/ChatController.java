package com.zerobase.plistbackend.module.websocket.controller;

import com.zerobase.plistbackend.module.websocket.domain.VideoSyncManager;
import com.zerobase.plistbackend.module.websocket.dto.request.VideoSyncRequest;
import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.request.VideoSyncResponse;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.websocket.exception.ChatException;
import com.zerobase.plistbackend.module.websocket.service.ChatService;
import com.zerobase.plistbackend.module.websocket.type.ChatErrorStatus;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Chatting API", description = "채팅과 관련된 API Controller")
public class ChatController {

  private final ChatService chatService;
  private final VideoSyncManager videoSyncManager;

  @Operation(
      summary = "자신과 같은 채널에 속한 인원들 끼리 채팅을 주고받을 수 있습니다.",
      description = "WebSocket 연결을 통해 메시지를 전송합니다. **주의:** 이 API는 반드시 WebSocket을 통해 호출해야 하며, REST 호출은 지원하지 않습니다."
  )
  @PostMapping("/chat.{channelId}")
  @MessageMapping("/chat.{channelId}")
  @SendTo("/sub/chat.{channelId}")
  // 해당 부분은 channel의 Participant의 정보를 클라가 갖고 있다면 보내면 될듯?? -> 노션 보면됨
  public ChatMessageResponse sendMessage(@Payload ChatMessageRequest request) {
    return chatService.sendMessage(request);
  }

  @Operation(
      summary = "클라이언트 측에서는 해당 Mapping 주소로 지속적으로 호스트가 보고있는 비디오의 currentTime을 서버로 보냅니다."
          + "그럼 구독자들은 비디오의 currntTime을 지속적으로 같이 받고 영상 시점을 공유합니다.",
      description = "WebSocket 연결을 통해 메시지를 전송합니다. **주의:** 이 API는 반드시 WebSocket을 통해 호출해야 하며, REST 호출은 지원하지 않습니다."
  )
  @PostMapping("/video.{channelId}")
  @MessageMapping("/video.{channelId}")
  @SendTo("/sub/video.{channelId}")
  public VideoSyncResponse syncVideo(@DestinationVariable Long channelId, @Payload VideoSyncRequest request) {
    videoSyncManager.updateCurrentTime(channelId, request.getCurrentTime());
    return new VideoSyncResponse(request);
  }

  @Operation(
      summary = "클라이언트 측에서 지속적으로 받은 호스트의 비디오 currentTime을 새로운 사용자가 입장시 호스트의 시점으로 같이 비디오가 동기화됩니다.",
      description = "WebSocket 연결을 통해 메시지를 전송합니다. **주의:** 이 API는 반드시 WebSocket을 통해 호출해야 하며, REST 호출은 지원하지 않습니다."
  )
  @PostMapping("/join.{channelId}")
  @MessageMapping("/join.{channelId}")
  @SendTo("/sub/video.{channelId}")
  public VideoSyncResponse syncVideoForNewUser(@DestinationVariable Long channelId, @Payload VideoSyncRequest request) {
    Long currentTime = videoSyncManager.getCurrentTime(channelId);
    log.info("New user joined channel {}" , currentTime);
    return new VideoSyncResponse(request);
  }

  @Operation(
      summary = "현재 재생중인 비디오의 재생 및 일시정지를 호스트만 요청할 수 있고 재생 및 일시정지의 시점을 사용자 모두 동기화 합니다.",
      description = "WebSocket 연결을 통해 메시지를 전송합니다. **주의:** 이 API는 반드시 WebSocket을 통해 호출해야 하며, REST 호출은 지원하지 않습니다."
  )
  @MessageMapping("/video.control.{channelId}")
  @SendTo("/sub/video.{channelId}")
  public VideoSyncResponse controlVideo(@DestinationVariable Long channelId,
      @Payload VideoSyncRequest request, @AuthenticationPrincipal CustomOAuth2User user) {

    if (!chatService.isHost(user)) {
      throw new ChatException(ChatErrorStatus.NOT_HOST);
    }
    videoSyncManager.updateCurrentTime(channelId, request.getCurrentTime());
    log.info("호스트가 채널 {}의 비디오 상태를 업데이트: 현재 시간={}", channelId, request.getCurrentTime());
    return new VideoSyncResponse(request);
  }

}
