package com.zerobase.plistbackend.module.chatting.controller;

import com.zerobase.plistbackend.module.chatting.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.chatting.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.chatting.dto.response.WelcomeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatController {

  /**
   * 같은 방 인원 끼리 채팅
   * @param request
   * @param chatRoomId
   */
  @MessageMapping("/chat.{chatRoomId}")
  @SendTo("/sub/chat.{chatRoomId}")
  public ChatMessageResponse sendMessage(@Payload ChatMessageRequest request, @DestinationVariable Long chatRoomId) {
    // TODO -> chatRoomId가 channel_id 여야 하니 이게 실제 현재 Live 가능한 상태인지 확인하고, 없다면 접근을 막거나 있다면 성공적으로 보내거나
    log.info("Chat Room Id : {} Message : {}", chatRoomId, request);
    return new ChatMessageResponse(request.getUsername(), request.getMessage());
  }


  /**
   * 방 입장시 환영 메시지 출력
   * @param request
   * @param chatRoomId
   */
  @MessageMapping("/chat.enter/{chatRoomId}")
  @SendTo("/sub/chat.{chatRoomId}")
  public WelcomeMessage enterChatRoom(@Payload ChatMessageRequest request, @DestinationVariable Long chatRoomId) {
    // TODO -> ChatMessageRequest 가 아닌 Principal 객체를 파라미터로 받아서 username 추출
    log.info("{} has entered the chat room {}", request.getUsername(), chatRoomId);
    return new WelcomeMessage(request.getUsername());
  }
}
