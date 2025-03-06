package com.zerobase.plistbackend.module.channel.controller;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.DetailChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.DetailClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import com.zerobase.plistbackend.module.channel.service.ChannelService;
import com.zerobase.plistbackend.module.channel.util.ControllerApiResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api")
@Tag(name = "Channel API", description = "채널과 관련된 API Controller")
public class ChannelController {

  private final ChannelService channelService;

  @Operation(
      summary = "채널 생성",
      description = "채널을 생성합니다."
  )
  @PostMapping("/channel")
  public ResponseEntity<DetailChannelResponse> addChannel(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @Valid @RequestBody ChannelRequest channelRequest) {

    DetailChannelResponse detailChannelResponse = channelService.addChannel(customOAuth2User,
        channelRequest);

    log.info("{}번 채널이 생성되었습니다.", detailChannelResponse.getChannelId());

    return ResponseEntity.status(HttpStatus.CREATED).body(detailChannelResponse);
  }

  @Operation(
      summary = "사용자 채널 입장",
      description = "채널ID와 일치하는 채널에 사용자가 입장합니다."
  )
  @PatchMapping("/channel/{channelId}")
  public ResponseEntity<DetailChannelResponse> userEnterChannel(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable Long channelId) {

    DetailChannelResponse detailChannelResponse = channelService.userEnterChannel(customOAuth2User,
        channelId);

    return ResponseEntity.status(HttpStatus.OK).body(detailChannelResponse);
  }

  @Operation(
      summary = "사용자 채널 퇴장",
      description = "채널ID와 일치하는 채널에서 사용자가 퇴장합니다. 해당 채널에 사용자는 참여하고 있어야합니다."
  )
  @PatchMapping("/channel/exit/{channelId}")
  public ResponseEntity<Void> userExitChannel(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable Long channelId) {

    channelService.userExitChannel(customOAuth2User, channelId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "호스트 채널 퇴장",
      description = "채널ID와 일치하는 채널에서 호스트가 퇴장합니다. 해당 채널의 호스트여야 합니다. "
          + "호스트가 채널을 퇴장하면 채널은 Closed 상태로 변하고, "
          + "해당 채널에 참여하고 있던 모든 참가자는 퇴장됩니다."
  )
  @PatchMapping("/channel/destroy/{channelId}")
  public ResponseEntity<Void> hostExitChannel(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable Long channelId) {

    channelService.hostExitChannel(customOAuth2User, channelId);

    log.info("{}번 채널이 종료되었습니다.", channelId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "채널 영상 추가",
      description = "채널ID와 일치하는 채널의 플레이리스트에 영상을 추가합니다. 해당 채널에 참여하고 있어야합니다."
  )
  @PatchMapping("/channel/{channelId}/add-video")
  public ResponseEntity<Void> addVideoToChannel(@PathVariable Long channelId,
      @RequestBody VideoRequest videoRequest,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    channelService.addVideoToChannel(channelId, videoRequest, customOAuth2User);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "채널 영상 삭제",
      description = "채널ID와 일치하는 채널의 플레이리스트에 id값을 가진 영상을 제거합니다."
          + " 해당 채널의 호스트여야 합니다."
  )
  @PatchMapping("/channel/{channelId}/delete-video")
  public ResponseEntity<Void> deleteVideoToChannel(@PathVariable Long channelId,
      @RequestParam("id") Long id, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    channelService.deleteVideoToChannel(channelId, id, customOAuth2User);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "채널 플레이리스트 순서 변경",
      description = "채널ID와 일치하는 채널의 플레이리스트의 순서를 변경합니다."
          + "이 API는 오직 프론트에서 받아온 변경된 플레이리스트를 현재 채널의 플레이리스트에 덮어씁니다."
  )
  @PatchMapping("/channel/{channelId}/update")
  public ResponseEntity<Void> updateChannelPlaylist(
      @PathVariable Long channelId,
      @RequestBody String updateChannelPlaylistJson,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    channelService.updateChannelPlaylist(channelId, updateChannelPlaylistJson, customOAuth2User);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "채널 플레이리스트를 내 플레이리스트에 저장",
      description = "채널ID와 일치하는 채널의 플레이리스트를 내 플레이리스트로 저장합니다."
          + " 내 플레이리스트로 저장될 때 내 플레이리스트의 이름은 채널의 이름으로 저장됩니다."
          + " 이 기능은 채널을 퇴장하고 플레이리스트를 저장할 경우 실행됩니다."
  )
  @PostMapping("/channel/{channelId}/save-playlist")
  public ResponseEntity<Void> savePlaylistToUserPlaylist(
      @PathVariable Long channelId,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    channelService.savePlaylistToUserPlaylist(channelId, customOAuth2User);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(
      summary = "좋아요를 누른 영상 favorite플레이리스트에 저장",
      description = "좋아요를 누르면 내 플레이리스트의 favorite 폴더로 저장됩니다."
  )
  @PostMapping("/user/favorite")
  public ResponseEntity<Void> likeVideo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestBody VideoRequest videoRequest) {

    channelService.likeVideo(customOAuth2User, videoRequest);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  // 검색 관련 컨트롤러

  @Operation(
      summary = "[최신순]현재 스트리밍 중인 모든 채널 조회",
      description = "현재 스트리밍 중인 모든 채널을 조회합니다. 정렬은 최신순으로 정렬됩니다."
  )
  @GetMapping("/channels")
  public ResponseEntity<ControllerApiResponse<StreamingChannelResponse>> findChannelList(
      @RequestParam(value = "cursorId", required = false) Long cursorId,
      @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {
    Slice<StreamingChannelResponse> streamingChannelResponseList = channelService.findChannelList(
        cursorId, pageable);

    return ResponseEntity.ok(new ControllerApiResponse<>(streamingChannelResponseList.getContent(),
        streamingChannelResponseList.hasNext()));
  }

  @Operation(
      summary = "[인기순]현재 스트리밍 중인 모든 채널 조회",
      description = "현재 스트리밍 중인 모든 채널을 조회합니다. 정렬은 시청자가 많은 순서로 정렬됩니다."
  )
  @GetMapping("/channels/popular")
  public ResponseEntity<ControllerApiResponse<StreamingChannelResponse>> findChannelListPopular(
      @RequestParam(value = "cursorId", required = false) Long cursorId,
      @RequestParam(value = "cursorPopular", required = false) Long cursorPopular,
      @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {
    Slice<StreamingChannelResponse> streamingChannelResponseList = channelService.findChannelListPopular(
        cursorId, cursorPopular, pageable);

    return ResponseEntity.ok(new ControllerApiResponse<>(streamingChannelResponseList.getContent(),
        streamingChannelResponseList.hasNext()));
  }

  //Todo : 기능개발
  @Operation(
      summary = "현재 스트리밍 중인 채널 검색 기능",
      description = "'채널 이름 & 채널 카테고리이름 & 채널 호스트닉네임' 이랑 검색어가 유사한 모든 채널을"
          + " 조회합니다. 정렬은 시청자가 많은 순서로 정렬됩니다."
  )
  @GetMapping("/channels/search")
  public ResponseEntity<List<StreamingChannelResponse>> searchChannel(
      @RequestParam("searchValue") String searchValue) {
    List<StreamingChannelResponse> streamingChannelResponseList = channelService.searchChannel(
        searchValue);

    return ResponseEntity.ok(streamingChannelResponseList);
  }

  @Operation(
      summary = "카테고리ID로 채널 조회",
      description = "카테고리ID와 일치하는 현재 스트리밍 중인 채널을 조회합니다. 정렬은 시청자가 많은 순서로 정렬됩니다."
  )
  @GetMapping("/channels/category/{categoryId}")
  public ResponseEntity<ControllerApiResponse<StreamingChannelResponse>> findChannelFromChannelCategory(
      @PathVariable Long categoryId,
      @RequestParam(value = "cursorId", required = false) Long cursorId,
      @RequestParam(value = "cursorPopular", required = false) Long cursorPopular,
      @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {
    Slice<StreamingChannelResponse> streamingChannelResponseList =
        channelService.findChannelFromChannelCategory(categoryId, cursorId, cursorPopular,
            pageable);

    return ResponseEntity.ok(new ControllerApiResponse<>(streamingChannelResponseList.getContent(),
        streamingChannelResponseList.hasNext()));
  }

  @Operation(
      summary = "[백엔드 테스트용]채널 상세 조회",
      description = "[백엔드 테스트용]채널ID과 일치하는 채널의 정보를 조회합니다."
  )
  @GetMapping("/channel/{channelId}")
  public ResponseEntity<DetailChannelResponse> findOneChannel(@PathVariable Long channelId,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    DetailChannelResponse detailChannelResponse = channelService.findOneChannel(channelId,
        customOAuth2User);
    return ResponseEntity.ok(detailChannelResponse);
  }

  @Operation(
      summary = "내 과거 호스트 내역 전체조회",
      description = "내 과거 호스트 내역을 전체조회합니다. 정렬은 최신순입니다."
  )
  @GetMapping("/user/history")
  public ResponseEntity<ControllerApiResponse<ClosedChannelResponse>> findUserChannelHistory(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestParam(value = "cursorId", required = false) Long cursorId,
      @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {

    Slice<ClosedChannelResponse> closedChannelResponsesList = channelService.findUserChannelHistory(
        customOAuth2User, cursorId, pageable);

    return ResponseEntity.ok(new ControllerApiResponse<>(closedChannelResponsesList.getContent(),
        closedChannelResponsesList.hasNext()));
  }

  @Operation(
      summary = "내 과거 호스트 내역 상세조회",
      description = "내 과거 호스트 내역을 상세 조회합니다."
  )
  @GetMapping("user/history/{channelId}")
  public ResponseEntity<DetailClosedChannelResponse> findOneUserChannelHistory(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User, @PathVariable Long channelId) {

    DetailClosedChannelResponse detailClosedChannelResponse = channelService.findOneUserChannelHistory(
        customOAuth2User, channelId);

    return ResponseEntity.ok(detailClosedChannelResponse);
  }
}