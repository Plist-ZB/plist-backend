package com.zerobase.plistbackend.module.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.channel.type.ChannelErrorStatus;
import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.request.NewUserWelcomeMessage;
import com.zerobase.plistbackend.module.websocket.dto.request.VideoControlRequest;
import com.zerobase.plistbackend.module.websocket.dto.request.VideoSyncRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.VideoControlResponse;
import com.zerobase.plistbackend.module.websocket.dto.response.VideoSyncResponse;
import com.zerobase.plistbackend.module.websocket.exception.WebSocketControllerException;
import com.zerobase.plistbackend.module.websocket.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {

  private final WebSocketService webSocketService;
  private final RedisChatPubSubService redisChatPubSubService;
  private final RedisVideoSyncService redisVideoSyncService;
  private final RedisNewUserEnterService redisNewUserEnterService;
  private final RedisVideoControlService redisVideoControlService;
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
    request.allocateChannelId(channelId);
    String syncData = mapper.writeValueAsString(new VideoSyncResponse(request));
    redisVideoSyncService.publish("videoSync",syncData);
  }

  @MessageMapping("/enter.{channelId}")
//     @SendTo("/sub/enter.{channelId}")
  public void enterNewUserForSync(@DestinationVariable Long channelId) throws JsonProcessingException {
    String welcomeMessage = mapper.writeValueAsString(new NewUserWelcomeMessage(channelId, "NEW_USER_ENTER"));
    redisNewUserEnterService.publish("newUserEnter", welcomeMessage);
  }
  
  @MessageMapping("/video.control.{channelId}")
//     @SendTo("/sub/video.{channelId}")
  public void controlVideo(@DestinationVariable Long channelId,
      @Payload VideoControlRequest request) throws JsonProcessingException {
    if (!webSocketService.isHost(channelId, request.getEmail())) {
      throw new WebSocketControllerException(ChannelErrorStatus.NOT_HOST);
    }
    request.allocateChannelId(channelId);
    String response = mapper.writeValueAsString(new VideoControlResponse(request));
    redisVideoControlService.publish("videoControl", response);
  }
}
