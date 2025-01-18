package com.zerobase.plistbackend.module.websocket.dto.response;

import com.zerobase.plistbackend.module.websocket.dto.request.VideoSyncRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoSyncResponse {
  private String videoId;
  private Long playStates;
  private Long currentTime;

  public VideoSyncResponse(VideoSyncRequest request) {
    this.videoId = request.getVideoId();
    this.playStates = request.getPlayState();
    this.currentTime = request.getCurrentTime();
  }
}
