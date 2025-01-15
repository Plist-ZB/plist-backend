package com.zerobase.plistbackend.module.websocket.dto.response;

import com.zerobase.plistbackend.module.websocket.dto.request.VideoSyncRequest;
import lombok.Getter;

@Getter
public class VideoSyncResponse {
  private final String videoId;
  private final Long playStates;
  private final Long currentTime;

  public VideoSyncResponse(VideoSyncRequest request) {
    this.videoId = request.getVideoId();
    this.playStates = request.getPlayState();
    this.currentTime = request.getCurrentTime();
  }
}
