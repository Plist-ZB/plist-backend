package com.zerobase.plistbackend.module.channel.controller;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ChannelResponse;
import com.zerobase.plistbackend.module.home.dto.response.VideoResponse;
import com.zerobase.plistbackend.module.channel.service.ChannelService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api")
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/channel") // TODO : Principal 본인이 채널 참가자로 등록되게 해야함.
  public ResponseEntity<ChannelResponse> addChannel(@RequestBody ChannelRequest channelRequest) {
    ChannelResponse channelResponse = channelService.addChannel(channelRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(channelResponse);
  }

  @GetMapping("/channels")
  public ResponseEntity<List<ChannelResponse>> findChannelList() {
    List<ChannelResponse> channelResponseList = channelService.findChannelList();

    return ResponseEntity.ok(channelResponseList);
  }

  @GetMapping("/channels/{channelName}")
  public ResponseEntity<List<ChannelResponse>> findChannelFromChannelName(@PathVariable String channelName) {
    List<ChannelResponse> channelResponseList = channelService.findChannelFromChannelName(
        channelName);

    return ResponseEntity.ok(channelResponseList);
  }

  @GetMapping("/channels/{channelCategory}")
  public ResponseEntity<List<ChannelResponse>> findChannelFromChannelCategory(@PathVariable String channelCategory) {
    List<ChannelResponse> channelResponseList = channelService.findChannelFromChannelCategory(
        channelCategory);

    return ResponseEntity.ok(channelResponseList);
  }
}
