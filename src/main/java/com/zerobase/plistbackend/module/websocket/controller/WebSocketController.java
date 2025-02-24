package com.zerobase.plistbackend.module.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.channel.type.ChannelErrorStatus;
import com.zerobase.plistbackend.module.websocket.domain.VideoSyncManager;
import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.request.VideoControlRequest;
import com.zerobase.plistbackend.module.websocket.dto.request.VideoSyncRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.VideoControlResponse;
import com.zerobase.plistbackend.module.websocket.dto.response.VideoSyncResponse;
import com.zerobase.plistbackend.module.websocket.exception.WebSocketControllerException;
import com.zerobase.plistbackend.module.websocket.service.RedisChatPubSubService;
import com.zerobase.plistbackend.module.websocket.service.RedisVideoSyncService;
import com.zerobase.plistbackend.module.websocket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebSocketController {

  private final WebSocketService webSocketService;
  private final VideoSyncManager videoSyncManager;
  private final RedisChatPubSubService redisChatPubSubService;
  private final RedisVideoSyncService redisVideoSyncService;
  private final ObjectMapper mapper;


  @MessageMapping("/chat.{channelId}")
//        @SendTo("/sub/chat.{channelId}") // 서버를 거치고 처리한 결과를 전송할 주소
  public void sendMessage(@DestinationVariable Long channelId, @Payload ChatMessageRequest request) throws JsonProcessingException {
    request.allocateChannelId(channelId);
    String message = mapper.writeValueAsString(webSocketService.sendMessage(request));
    redisChatPubSubService.publish("chat", message);
  }

  @MessageMapping("/video.{channelId}")
//        @SendTo("/sub/video.{channelId}")
  public void syncVideo(@DestinationVariable Long channelId,
      @Payload VideoSyncRequest request) throws JsonProcessingException {
    videoSyncManager.updateCurrentTime(channelId, request.getCurrentTime());
    String syncData = mapper.writeValueAsString(new VideoSyncResponse(request));
    redisVideoSyncService.publish("videoSync",syncData);
  }

  @MessageMapping("/enter.{channelId}")
//     @SendTo("/sub/enter.{channelId}")
  public String enterNewUserForSync() {
    return "NEW_USER_ENTER";
  }
  
  @MessageMapping("/video.control.{channelId}")
//     @SendTo("/sub/video.{channelId}")
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
