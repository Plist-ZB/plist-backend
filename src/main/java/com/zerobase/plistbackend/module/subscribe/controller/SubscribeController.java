package com.zerobase.plistbackend.module.subscribe.controller;

import com.zerobase.plistbackend.module.subscribe.dto.response.SubscribeResponse;
import com.zerobase.plistbackend.module.subscribe.service.SubscribeService;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api")
@Tag(name = "Subscribe Service API", description = "구독과 관련된 API Controller")
public class SubscribeController {

  private final SubscribeService subscribeService;

  @Operation(
      summary = "사용자 구독리스트 전체 조회",
      description = "사용자의 구독리스트를 전체 조회합니다."
  )
  @GetMapping("/subscribe/followees")
  public ResponseEntity<SubscribeResponse> findFollowees(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    SubscribeResponse response = subscribeService.findFollowees(customOAuth2User);

    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "구독하기",
      description = "사용자가 한 사용자를 구독합니다."
  )
  @PostMapping("/subscribe")
  public ResponseEntity<Void> subscribe(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestBody Long followeeId) {

    subscribeService.subcribe(customOAuth2User, followeeId);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(
      summary = "구독 취소하기",
      description = "사용자가 한 사용자의 구독을 취소합니다."
  )
  @DeleteMapping("/unsubscribe")
  public ResponseEntity<Void> unsubscribe(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody Long followeeId) {

    subscribeService.unsubscribe(customOAuth2User, followeeId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}