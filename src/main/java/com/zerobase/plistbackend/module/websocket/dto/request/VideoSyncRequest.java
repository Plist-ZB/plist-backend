package com.zerobase.plistbackend.module.websocket.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VideoSyncRequest {
  private String videoId;
  private Long currentTime;
  private Long playState;
}
