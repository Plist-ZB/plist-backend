package com.zerobase.plistbackend.module.user.controller;

import com.zerobase.plistbackend.module.user.dto.request.UserProfileRequest;
import com.zerobase.plistbackend.module.user.dto.response.HostPlaytimeResponse;
import com.zerobase.plistbackend.module.user.dto.response.OtherProfileResponse;
import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;
import com.zerobase.plistbackend.module.user.dto.response.UserPlaytimeResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api")
@Tag(name = "User Service API", description = "회원과 관련된 API Controller")
public class UserController {

  private final UserServiceImpl userService;

  @Operation(summary = "내 정보 조회", description = "로그인된 회원의 정보를 조회할 수 있습니다.")
  @GetMapping("/user/profile")
  public ResponseEntity<ProfileResponse> findMyProfile(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    return ResponseEntity.ok(userService.findProfile(customOAuth2User.findEmail()));
  }

  @Operation(summary = "다른 회원 정보 조회", description = "요청한 회원의 정보를 조회할 수 있습니다.")
  @GetMapping("/user/profile/{userId}")
  public ResponseEntity<OtherProfileResponse> findOtherProfile(
      @PathVariable Long userId) {
    return ResponseEntity.ok(userService.findUserIdProfile(userId));
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
    return ResponseEntity.ok(
        userService.editProfile(userProfileRequest, customOAuth2User.findId()));
  }

  @Operation(summary = "회원의 플리방 이력 정보 가져오기",
      description = "연도별 호스트 재생 시간, 전체 참여자수, 팔로워 수(팔로워 수는 기능이 완료되면 추가 예정)," +
          " 쿼리 스트링으로 년도만 입력 ex)2024 입력시 2024~2025 데이터 추출")
  @GetMapping("/me/playtime/host")
  public ResponseEntity<HostPlaytimeResponse> getHistoryOfHost(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestParam("year") int year) {
    return ResponseEntity.ok(userService.getHistoryOfHost(customOAuth2User.findId(), year));
  }

  @Operation(summary = "회원의 플리방 이력 정보 가져오기",
      description = "연도별 플리방 이용시간" +
          " 쿼리 스트링으로 년도만 입력 ex)2024 입력시 2024~2025 데이터 추출")
  @GetMapping("/me/playtime/user")
  public ResponseEntity<UserPlaytimeResponse> getHistoryOfUser(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestParam("year") int year) {
    return ResponseEntity.ok(userService.getHistoryOfUser(customOAuth2User.findId(), year));
  }
}