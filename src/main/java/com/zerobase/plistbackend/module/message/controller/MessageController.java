package com.zerobase.plistbackend.module.message.controller;

import com.zerobase.plistbackend.module.message.dto.response.MessageResponse;
import com.zerobase.plistbackend.module.message.dto.response.UnreadResponse;
import com.zerobase.plistbackend.module.message.service.MessageService;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api")
@Tag(name = "Message Service API", description = "알림 메시지에 관련된 API Controller")
public class MessageController {

  private final MessageService messageService;

  @Operation(
      summary = "사용자 알림메시지 전체조회",
      description = "사용자의 알림메시지 전체를 조회합니다."
  )
  @GetMapping("/messages")
  public ResponseEntity<List<MessageResponse>> findMessages(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    List<MessageResponse> response = messageService.findMessages(customOAuth2User);

    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "사용자 메시지 단건 읽음처리",
      description = "사용자의 알림메시지를 읽음처리합니다."
  )
  @PatchMapping("/message/{messageId}")
  public ResponseEntity<Void> readMessage(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable(value = "messageId") Long messageId) {

    messageService.readMessage(customOAuth2User, messageId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "사용자 메시지 전체 읽음처리",
      description = "사용자의 알림메시지를 전체 읽음처리합니다."
  )
  @PatchMapping("/messages")
  public ResponseEntity<Void> readAllMessage(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    messageService.readAllMessage(customOAuth2User);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "사용자 메시지 안읽음 표시",
      description = "사용자가 읽지 않은 메시지가 있는지 확인합니다."
  )
  @GetMapping("/messages/unread")
  public ResponseEntity<UnreadResponse> checkUnreadMessage(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    UnreadResponse response = messageService.checkUnreadMessage(customOAuth2User);

    return ResponseEntity.ok(response);
  }
}
