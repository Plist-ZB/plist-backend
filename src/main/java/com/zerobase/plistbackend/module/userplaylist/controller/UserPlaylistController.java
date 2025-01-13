package com.zerobase.plistbackend.module.userplaylist.controller;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.UserPlaylistRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.response.DetailUserPlaylistResponse;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import com.zerobase.plistbackend.module.userplaylist.service.UserPlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api")
@Tag(name = "UserPlaylist API", description = "내 플레이리스트와 관련된 API Controller")
public class UserPlaylistController {
/*asd*/
  private final UserPlaylistService userPlaylistService;

  @Operation(
      summary = "내 플레이리스트 생성",
      description = "내 플레이리스트를 생성합니다."
  )
  @PostMapping("/user/playlist")
  public ResponseEntity<Void> createUserPlaylist(
      @Valid @RequestBody UserPlaylistRequest userPlaylistRequest,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    userPlaylistService.createUserPlayList(userPlaylistRequest, customOAuth2User);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(
      summary = "내 플레이리스트 전체 조회",
      description = "내 플레이리스트의 모든 플레이리스트를 조회합니다."
  )
  @GetMapping("/user/playlists")
  public ResponseEntity<List<UserPlaylistResponse>> findAllUserPlaylist(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    List<UserPlaylistResponse> userPlaylistResponseList = userPlaylistService.findAllUserPlaylist(
        customOAuth2User);

    return ResponseEntity.ok(userPlaylistResponseList);
  }

  @Operation(
      summary = "내 플레이리스트 상세 조회",
      description = "userPlaylistId값과 일치한 내 플레이리스트를 조회합니다."
  )
  @GetMapping("/user/playlist/{userPlaylistId}")
  public ResponseEntity<DetailUserPlaylistResponse> findOneUserPlaylist(
      @PathVariable Long userPlaylistId,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    DetailUserPlaylistResponse detailUserPlaylistResponse = userPlaylistService.findOneUserPlaylist(
        userPlaylistId, customOAuth2User);

    return ResponseEntity.ok(detailUserPlaylistResponse);
  }

  @Operation(
      summary = "내 플레이리스트 삭제",
      description = "userPlaylistId값과 일치한 내 플레이리스트를 삭제합니다."
  )
  @DeleteMapping("/user/playlist/{userPlaylistId}")
  public ResponseEntity<Void> deleteUserPlaylist(@PathVariable Long userPlaylistId,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    userPlaylistService.deleteUserPlaylist(userPlaylistId, customOAuth2User);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "내 플레이리스트에 영상 추가",
      description = "userPlaylistId값과 일치한 내 플레이리스트에 영상을 추가합니다."
  )
  @PatchMapping("/user/playlist/{userPlaylistId}/add")
  public ResponseEntity<Void> addVideoToUserPlaylist(
      @PathVariable Long userPlaylistId,
      @RequestBody VideoRequest videoRequest,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    userPlaylistService.addVideo(customOAuth2User, userPlaylistId, videoRequest);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "내 플레이리스트에서 영상 삭제",
      description = "userPlaylistId값과 일치한 내 플레이리스트에서 id값에 해당하는 영상을 삭제합니다."
  )
  @PatchMapping("/user/playlist/{userPlaylistId}/remove")
  public ResponseEntity<Void> removeVideoToUserPlaylist(
      @PathVariable Long userPlaylistId,
      @RequestParam("id") Long id,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    userPlaylistService.removeVideo(customOAuth2User, userPlaylistId, id);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "내 플레이리스트 영상 순서 변경",
      description = "UserPlaylistId와 일치하는 내 플레이리스트의 영상 순서를 변경합니다."
          + "이 API는 오직 프론트에서 받아온 플레이리스트를 현재의 플레이리스트에 덮어씁니다."
  )
  @PatchMapping("/user/playlist/{userPlaylistId}/update")
  public ResponseEntity<Void> updateUserPlaylist(
      @PathVariable Long userPlaylistId,
      @RequestBody String updateUserPlaylistJson,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    userPlaylistService.updateUserPlaylist(userPlaylistId, updateUserPlaylistJson,
        customOAuth2User);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "내 플레이리스트 이름 변경",
      description = "UserPlaylistId와 일치하는 내 플레이리스트의 이름을 변경합니다."
  )
  @PatchMapping("/user/playlist/{userPlaylistId}")
  public ResponseEntity<Void> changeUserPlaylistName(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestBody UserPlaylistRequest userPlaylistRequest, @PathVariable Long userPlaylistId) {

    userPlaylistService.changeUserPlaylistName(customOAuth2User, userPlaylistRequest, userPlaylistId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
