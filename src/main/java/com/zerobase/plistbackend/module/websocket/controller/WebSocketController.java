package com.zerobase.plistbackend.module.websocket.controller;

import com.zerobase.plistbackend.module.channel.type.ChannelErrorStatus;
import com.zerobase.plistbackend.module.websocket.domain.VideoSyncManager;
import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.request.VideoControlRequest;
import com.zerobase.plistbackend.module.websocket.dto.request.VideoSyncRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.websocket.dto.response.VideoControlResponse;
import com.zerobase.plistbackend.module.websocket.dto.response.VideoSyncResponse;
import com.zerobase.plistbackend.module.websocket.exception.WebSocketControllerException;
import com.zerobase.plistbackend.module.websocket.service.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "WebSocket API", description = "WebSocket 관련된 API Controller")
public class WebSocketController {

  private final WebSocketService webSocketService;
  private final VideoSyncManager videoSyncManager;


  @PostMapping("/chat.{channelId}")
  @SendTo("/sub/chat.{channelId}") // 서버를 거치고 처리한 결과를 전송할 주소
  public ChatMessageResponse sendMessage(@Payload ChatMessageRequest request) {
    return webSocketService.sendMessage(request);
  }

  @MessageMapping("/video.{channelId}")
  @SendTo("/sub/video.{channelId}")
  public VideoSyncResponse syncVideo(@DestinationVariable Long channelId,
      @Payload VideoSyncRequest request) {
    videoSyncManager.updateCurrentTime(channelId, request.getCurrentTime());
    return new VideoSyncResponse(request);
  }

  @MessageMapping("/enter.{channelId}")
  @SendTo("/sub/enter.{channelId}")
  public String enterNewUserForSync() {
    return "NEW_USER_ENTER";
  }
  
  @MessageMapping("/video.control.{channelId}")
  @SendTo("/sub/video.{channelId}")
  public VideoControlResponse controlVideo(@DestinationVariable Long channelId,
      @Payload VideoControlRequest request) {
    if (!webSocketService.isHost(channelId, request.getEmail())) {
      throw new WebSocketControllerException(ChannelErrorStatus.NOT_HOST);
    }
    videoSyncManager.updateCurrentTime(channelId, request.getCurrentTime());
    log.info("호스트가 채널 {}의 비디오 상태를 업데이트: 현재 시간={}", channelId, request.getCurrentTime());
    return new VideoControlResponse(request);
  }
}
