package com.zerobase.plistbackend.module.websocket.dto.response;

import com.zerobase.plistbackend.module.websocket.dto.request.VideoSyncRequest;
import com.zerobase.plistbackend.module.websocket.dto.videointerface.VideoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoSyncResponse implements VideoResponse {
  private String type;
  private String videoId;
  private Long playStates;
  private Long currentTime;
  private Long channelId;

  public VideoSyncResponse(VideoSyncRequest request) {
    this.type = "VIDEO_STATE";
    this.channelId = request.getChannelId();
    this.videoId = request.getVideoId();
    this.playStates = request.getPlayState();
    this.currentTime = request.getCurrentTime();
  }
}
