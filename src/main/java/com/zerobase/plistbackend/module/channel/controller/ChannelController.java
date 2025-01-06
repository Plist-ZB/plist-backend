package com.zerobase.plistbackend.module.channel.controller;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ChannelResponse;
import com.zerobase.plistbackend.module.channel.service.ChannelService;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/channel")
  // TODO : Principal 본인이 채널 참가자로 등록되게 해야함. || 채널 생성 시 호스트가 재생목록을 추가하면 추가되도록 해야함.
  public ResponseEntity<ChannelResponse> addChannel(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestBody ChannelRequest channelRequest) {

    ChannelResponse channelResponse = channelService.addChannel(customOAuth2User, channelRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(channelResponse);
  }

  @GetMapping("/channels")
  public ResponseEntity<List<ChannelResponse>> findChannelList() {
    List<ChannelResponse> channelResponseList = channelService.findChannelList();

    return ResponseEntity.ok(channelResponseList);
  }

  // TODO : 채널 아이디값으로 채널 검색.

  // TODO : 홈에서 채널 검색할 시, 호스트네임, 카테고리, 채널명까지 포함해서 검색결과 반환.

  @GetMapping("/channels/channel-name")
  public ResponseEntity<List<ChannelResponse>> findChannelFromChannelName(
      @RequestParam("searchValue") String channelName) {
    List<ChannelResponse> channelResponseList = channelService.findChannelFromChannelName(
        channelName);

    return ResponseEntity.ok(channelResponseList);
  } // TODO : 합쳐질 예정

  @GetMapping("/channels/channel-category")
  public ResponseEntity<List<ChannelResponse>> findChannelFromChannelCategory(
      @RequestParam("searchValue") String channelCategory) {
    List<ChannelResponse> channelResponseList = channelService.findChannelFromChannelCategory(
        channelCategory);

    return ResponseEntity.ok(channelResponseList);
  } // TODO : 카테고리 네임 -> 카테고리 ID로 검색

  @PatchMapping("/channel/{channelId}")
  public void enterChannel(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable Long channelId) {

    channelService.enterChannel(customOAuth2User, channelId);
  }

  @PatchMapping("/channel/exit/{channelId}")
  public void userExitChannel(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable Long channelId) {

    channelService.userExitChannel(customOAuth2User, channelId);
  }

  @PatchMapping("/channel/destroy/{channelId}")
  public void hostExitChannel(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable Long channelId) {

    channelService.hostExitChannel(customOAuth2User, channelId);
  }

  @PatchMapping("/channel/{channelId}/add-video")
  public ResponseEntity<ChannelResponse> addVideoToChannel(@PathVariable Long channelId,
      @RequestBody VideoRequest videoRequest,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    ChannelResponse channelResponse = channelService.addVideoToChannel(channelId, videoRequest,
        customOAuth2User);

    return ResponseEntity.ok(channelResponse);
  }

  @PatchMapping("/channel/{channelId}/delete-video")
  public ResponseEntity<ChannelResponse> deleteVideoToChannel(@PathVariable Long channelId,
      @RequestParam("id") Long id, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    ChannelResponse channelResponse = channelService.deleteVideoToChannel(channelId, id,
        customOAuth2User);

    return ResponseEntity.ok(channelResponse);
  }

  @PostMapping("/channel/{channelId}/save-playlist")
  public ResponseEntity<UserPlaylistResponse> savePlaylistToUserPlaylist(
      @PathVariable Long channelId,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    UserPlaylistResponse userPlaylistResponse = channelService.savePlaylistToUserPlaylist(channelId,
        customOAuth2User);

    return ResponseEntity.status(HttpStatus.CREATED).body(userPlaylistResponse);
  }

  //TODO : 특정 채널 플레이리스트 목록 조회.
  //TODO : 채널 플레이리스트 순서 변경.
}


