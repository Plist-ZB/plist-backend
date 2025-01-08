package com.zerobase.plistbackend.module.chatting.dto.request;

import com.zerobase.plistbackend.module.chatting.dto.VideoSyncRequest;
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
