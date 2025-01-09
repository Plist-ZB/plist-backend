package com.zerobase.plistbackend.module.chatting.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VideoSyncRequest {
  private String videoId;
  private Long currentTime;
  private Long playState;
}
