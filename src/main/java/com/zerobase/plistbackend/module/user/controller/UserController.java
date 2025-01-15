package com.zerobase.plistbackend.module.user.controller;

import com.zerobase.plistbackend.module.user.dto.request.UserProfileRequest;
import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api")
@Tag(name = "User Service API", description = "회원 정보 관리")
public class UserController {

  private final UserServiceImpl userService;

  @Operation(summary = "회원 정보 조회", description = "해당 회원의 정보를 조회할 수 있습니다.")
  @GetMapping("/user/profile")
  public ResponseEntity<ProfileResponse> findProfile(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    return ResponseEntity.ok(userService.findProfile(customOAuth2User.findEmail()));
  }

  @Operation(summary = "회원 탈퇴", description = "해당 회원의 탈퇴를 진행할 수 있습니다.")
  @DeleteMapping("/user/withdraw")
  public ResponseEntity<String> withdraw(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    userService.withdrawUser(customOAuth2User.findId());
    return ResponseEntity.ok("User withdrawal successful.");
  }

  @Operation(summary = "회원 정보 수정", description = "해당 회원의 프로필 업데이트를 진행합니다.")
  @PatchMapping(value = "/user/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ProfileResponse> editProfile(
      @RequestPart(value = "image", required = false) MultipartFile file,
      @RequestPart(value = "nickname", required = false) String nickName,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    UserProfileRequest userProfileRequest = new UserProfileRequest(file, nickName);
    userProfileRequest.validate();
    return ResponseEntity.ok(userService.editProfile(userProfileRequest, customOAuth2User.findId()));
  }
}