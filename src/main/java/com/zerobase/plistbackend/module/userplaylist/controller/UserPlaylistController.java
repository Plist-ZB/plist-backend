package com.zerobase.plistbackend.module.userplaylist.controller;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import com.zerobase.plistbackend.module.userplaylist.service.UserPlaylistService;
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

public class UserPlaylistController {

  private final UserPlaylistService userPlaylistService;

  @PostMapping("/user/playlist")
  public ResponseEntity<UserPlaylistResponse> createUserPlaylist(
      @RequestBody String userPlaylistName,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    UserPlaylistResponse userPlaylistResponse = userPlaylistService.createUserPlayList(
        userPlaylistName,
        customOAuth2User);

    return ResponseEntity.status(HttpStatus.CREATED).body(userPlaylistResponse);
  }

  @GetMapping("/user/playlists")
  public ResponseEntity<List<UserPlaylistResponse>> findAllUserPlaylist(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    List<UserPlaylistResponse> userPlaylistResponseList = userPlaylistService.findAllUserPlaylist(
        customOAuth2User);

    return ResponseEntity.ok(userPlaylistResponseList);
  }

  @GetMapping("/user/playlist/{userPlaylistId}")
  public ResponseEntity<UserPlaylistResponse> findOneUserPlaylist(
      @PathVariable Long userPlaylistId,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    UserPlaylistResponse userPlaylistResponse = userPlaylistService.findOneUserPlaylist(
        userPlaylistId, customOAuth2User);

    return ResponseEntity.ok(userPlaylistResponse);
  }

  @DeleteMapping("/user/playlist/{userPlaylistId}")
  public void deleteUserPlaylist(@PathVariable Long userPlaylistId,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    userPlaylistService.deleteUserPlaylist(userPlaylistId, customOAuth2User);
  }

  @PatchMapping("/user/playlist/{userPlaylistId}/add")
  public ResponseEntity<UserPlaylistResponse> addVideoToUserPlaylist(
      @PathVariable Long userPlaylistId,
      @RequestBody VideoRequest videoRequest,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    UserPlaylistResponse userPlaylistResponse = userPlaylistService.addVideo(customOAuth2User,
        userPlaylistId, videoRequest);

    return ResponseEntity.ok(userPlaylistResponse);
  }

  @PatchMapping("/user/playlist/{userPlaylistId}/remove")
  public ResponseEntity<UserPlaylistResponse> removeVideoToUserPlaylist(
      @PathVariable Long userPlaylistId,
      @RequestParam("id") Long id,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    UserPlaylistResponse userPlaylistResponse = userPlaylistService.removeVideo(customOAuth2User,
        userPlaylistId, id);

    return ResponseEntity.ok(userPlaylistResponse);
  }

  // TODO: 유저플레이리스트 순서 변경.
}
