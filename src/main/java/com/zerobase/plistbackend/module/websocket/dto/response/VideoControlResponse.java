package com.zerobase.plistbackend.module.websocket.dto.response;

import com.zerobase.plistbackend.module.websocket.dto.request.VideoControlRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoControlResponse {
  private String videoId;
  private Long playStates;
  private Long currentTime;

  public VideoControlResponse(VideoControlRequest request) {
    this.videoId = request.getVideoId();
    this.playStates = request.getPlayState();
    this.currentTime = request.getCurrentTime();
  }
}
