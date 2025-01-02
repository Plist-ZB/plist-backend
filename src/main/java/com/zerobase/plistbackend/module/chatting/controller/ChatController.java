package com.zerobase.plistbackend.module.chatting.controller;

import com.zerobase.plistbackend.module.chatting.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.chatting.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.chatting.dto.response.WelcomeMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Chatting API", description = "채팅과 관련된 API Controller")
public class ChatController {


  @Operation(
      summary = "자신과 같은 채널에 속한 인원들 끼리 채팅을 주고받을 수 있습니다.",
      description = "WebSocket 연결을 통해 메시지를 전송합니다. **주의:** 이 API는 반드시 WebSocket을 통해 호출해야 하며, REST 호출은 지원하지 않습니다."
  )
  @PostMapping("/chat.{chatRoomId}")
  @MessageMapping("/chat.{chatRoomId}")
  @SendTo("/sub/chat.{chatRoomId}")
  public ChatMessageResponse sendMessage(@Payload ChatMessageRequest request, @DestinationVariable Long chatRoomId) {
    // TODO -> chatRoomId가 channel_id 여야 하니 이게 실제 현재 Live 가능한 상태인지 확인하고, 없다면 접근을 막거나 있다면 성공적으로 보내거나
    log.info("Chat Room Id : {} Message : {}", chatRoomId, request);
    return new ChatMessageResponse(request.getUsername(), request.getMessage());
  }


  @Operation(
      summary = "채널 입장시 환영 메시지를 출력합니다",
      description = "WebSocket 연결을 통해 메시지를 전송합니다. **주의:** 이 API는 반드시 WebSocket을 통해 호출해야 하며, REST 호출은 지원하지 않습니다."
  )
  @PostMapping("/chat.enter/{chatRoomId}")
  @MessageMapping("/chat.enter/{chatRoomId}")
  @SendTo("/sub/chat.{chatRoomId}")
  public WelcomeMessage enterChatRoom(Principal principal, @DestinationVariable Long chatRoomId) {
    // TODO -> ChatMessageRequest 가 아닌 Principal 객체를 파라미터로 받아서 username 추출
    log.info("{} has entered the chat room {}", principal.getName(), chatRoomId);
    return new WelcomeMessage(principal.getName());
  }
}
