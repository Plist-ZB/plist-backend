package com.zerobase.plistbackend.module.fcm.controller;

import com.zerobase.plistbackend.module.fcm.service.FCMTokenService;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api")
@Tag(name = "FCMToken Service API", description = "FCMToken과 관련된 API Controller")
public class FCMTokenController {

  private final FCMTokenService fcmTokenService;

  @Operation(
      summary = "사용자 FCMToken 저장",
      description = "일치하는 FCMToken이 존재하는 경우는 생성시간을 업데이트하고, 존재하지 않는 경우에는 저장한다."
  )
  @PostMapping("/fcm/token")
  public ResponseEntity<Void> createFCMToken(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody String token) {

    fcmTokenService.upsertFCMToken(customOAuth2User, token);

    log.info("FCMToken upsert now! target user email: {}", customOAuth2User.findEmail());

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(
      summary = "사용자 FCMToken 삭제",
      description = "요청한 FCMToken을 서버에서 삭제합니다."
  )
  @DeleteMapping("/fcm/token")
  public ResponseEntity<Void> deleteFCMToken(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody String token) {

    fcmTokenService.deleteFCMToken(customOAuth2User, token);

    log.info("FCMToken delete now! target user email: {}", customOAuth2User.findEmail());

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}