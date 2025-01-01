package com.zerobase.plistbackend.module.channel.controller;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.VideoResponse;
import com.zerobase.plistbackend.module.channel.service.ChannelService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/channels")
public class ChannelController {

  private final ChannelService channelService;

  /**
   * 채널 생성
   *
   * @param channelRequest (channelName, channelCategory, channelThumbnail, channelCapacity)
   * @return channelResponse ( channelId, channelName, channelCategory, channelThumbnail,
   * channelCreatedAt, channelFinishedAt, channelCapacity, channelPlaylists, channelParticipants)
   */
  @PostMapping() // TODO : Principal 본인이 채널 참가자로 등록되게 해야함.
  public ResponseEntity<?> addChannel(@RequestBody ChannelRequest channelRequest) {
    ChannelResponse channelResponse = channelService.addChannel(channelRequest);

    return ResponseEntity.ok(channelResponse);
  }

  /**
   * 현재 방송중인 채널 목록
   *
   * @return channelResponse ( channelId, channelName, channelCategory, channelThumbnail,
   * channelCreatedAt, channelFinishedAt, channelCapacity, channelPlaylists, channelParticipants)
   */
  @GetMapping()
  public ResponseEntity<?> findChannelList() {
    List<ChannelResponse> channelResponseList = channelService.findChannelList();

    return ResponseEntity.ok(channelResponseList);
  }

  /**
   * 채널 이름으로 현재 방송중인 채널 검색결과
   *
   * @param channelName
   * @return channelResponse ( channelId, channelName, channelCategory, channelThumbnail,
   * channelCreatedAt, channelFinishedAt, channelCapacity, channelPlaylists, channelParticipants)
   */
  @GetMapping("/{channelName}")
  public ResponseEntity<?> findChannelFromChannelName(@PathVariable String channelName) {
    List<ChannelResponse> channelResponseList = channelService.findChannelFromChannelName(
        channelName);

    return ResponseEntity.ok(channelResponseList);
  }

  /**
   * 채널 카테고리로 현재 방송중인 채널 검색결과
   *
   * @param channelCategory
   * @return channelResponse ( channelId, channelName, channelCategory, channelThumbnail,
   * channelCreatedAt, channelFinishedAt, channelCapacity, channelPlaylists, channelParticipants)
   */
  @GetMapping("/{channelCategory}")
  public ResponseEntity<?> findChannelFromChannelCategory(@PathVariable String channelCategory) {
    List<ChannelResponse> channelResponseList = channelService.findChannelFromChannelCategory(
        channelCategory);

    return ResponseEntity.ok(channelResponseList);
  }

  /**
   * 채널 내 사용자 영상 검색 기능
   *
   * @param searchValue
   * @return VideoResponseList ( VideoResponse : id, videoId, videoName, videoThumbnail,
   * videoDescription )
   */
  @GetMapping("/service/search")
  public ResponseEntity<?> searchVideo(@RequestParam("searchValue") String searchValue) {

    List<VideoResponse> videoResponseList = channelService.searchVideo(searchValue);

    return ResponseEntity.ok(videoResponseList);
  }
}
