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


  @Operation(
      summary = "자신과 같은 채널에 속한 인원들 끼리 채팅을 주고받을 수 있습니다.",
      description = "WebSocket 연결을 통해 메시지를 전송합니다.REST 호출은 지원하지 않습니다."
          + "-> 클라이언트가 데이터를 서버로 전송할 주소 /chat.{channelId}, 서버를 거치고 처리한 결과를 전송할 주소 /sub/chat.{channelId}"
  )
  @PostMapping("/chat.{channelId}")
  @MessageMapping("/chat.{channelId}") // 클라이언트가 메세지를 서버로 전송할 주소
  @SendTo("/sub/chat.{channelId}") // 서버를 거치고 처리한 결과를 전송할 주소
  public ChatMessageResponse sendMessage(@Payload ChatMessageRequest request) {
    return webSocketService.sendMessage(request);
  }

  @Operation(
      summary = "클라이언트 측에서는 해당 Mapping 주소로 지속적으로 호스트가 보고있는 비디오의 currentTime을 서버로 보냅니다."
          + "그럼 구독자들은 비디오의 currntTime을 지속적으로 같이 받고 영상 시점을 공유합니다.",
      description = "WebSocket 연결을 통해 메시지를 전송합니다. REST 호출은 지원하지 않습니다."
          + "-> 클라이언트가 데이터를 서버로 전송할 주소 /video.{channelId}, 서버를 거치고 처리한 결과를 전송할 주소 /sub/video.{channelId}"
  )
  @PostMapping("/video.{channelId}")
  @MessageMapping("/video.{channelId}")
  @SendTo("/sub/video.{channelId}")
  public VideoSyncResponse syncVideo(@DestinationVariable Long channelId,
      @Payload VideoSyncRequest request) {
    videoSyncManager.updateCurrentTime(channelId, request.getCurrentTime());
    return new VideoSyncResponse(request);
  }

  @Operation(
      summary = "클라이언트 측에서 지속적으로 받은 호스트의 비디오 currentTime을 새로운 사용자가 입장시 호스트의 시점으로 같이 비디오가 동기화됩니다.",
      description = "WebSocket 연결을 통해 메시지를 전송합니다. REST 호출은 지원하지 않습니다."
          + "-> 클라이언트가 데이터를 서버로 전송할 주소 /join.{channelId}, 서버를 거치고 처리한 결과를 전송할 주소 /sub/video.{channelId}"
  )
/*   @PostMapping("/join.{channelId}")
  @MessageMapping("/join.{channelId}")
  @SendTo("/sub/video.{channelId}")
  public VideoSyncResponse syncVideoForNewUser(@DestinationVariable Long channelId,
      @Payload VideoSyncRequest request) {
    Long currentTime = videoSyncManager.getCurrentTime(channelId);
    log.info("New user joined channel {}", currentTime);
    return new VideoSyncResponse(request);
  } */
  @PostMapping("enter.{channelId}") // 참여자가 입장하자마자 한번만 딱 보냄
  @MessageMapping("/enter.{channelId}")
  @SendTo("/sub/enter.{channelId}") // 호스트만 구독함
  public String enterNewUserForSync(@DestinationVariable Long channelId) {
    
    String message = "ENTER_NEW_USER";
    
    return message;
  }

  @Operation(
      summary = "현재 재생중인 비디오의 재생 및 일시정지를 호스트만 요청할 수 있고 재생 및 일시정지의 시점을 사용자 모두 동기화 합니다.",
      description = "WebSocket 연결을 통해 메시지를 전송합니다. REST 호출은 지원하지 않습니다."
          + "-> 클라이언트가 데이터를 서버로 전송할 주소 /video.control.{channelId}, 서버를 거치고 처리한 결과를 전송할 주소 /sub/video.{channelId}"
  )
  @PostMapping("/video.control.{channelId}")
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
  @Operation(
      summary = "채널의 플레이리스트에서 변경 감지 시 작동, 채널에 속하는 플레이리스트의 최신 DB정보를 가져와 참여자들에게 전달",
      description = "해당 엔드포인트는 /channel/{channelId}/add-video, /channel/{channelId}/update-video, /channel/{channelId}/delete-video 호출 시 작동"
          + "결과를 전송할 엔드포인트 /sub/video.{channelId}"
  )
  @PostMapping("/trigger")
  public void emptyVoid () {
    // Swagger 문서화를 위한 메서드 동작 X
  }
}
