package com.zerobase.plistbackend.module.user.controller;

import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api")
@Tag(name = "User Service API", description = "회원 정보 관리")
public class UserController {

  private final UserService userService;

  // 테스트용 컨트롤러입니다
  @GetMapping("/test")
  public String test() {
    return "Welcome to the test page!";
  }

  @Operation(summary = "회원 정보 조회", description = "해당 회원의 정보를 조회할 수 있습니다.")
  @GetMapping("/user/profile")
  public ResponseEntity<ProfileResponse> findProfile(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    return ResponseEntity.ok(userService.findProfile(customOAuth2User.findEmail()));
  }

}