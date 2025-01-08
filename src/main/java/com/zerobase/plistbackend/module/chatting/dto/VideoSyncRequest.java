package com.zerobase.plistbackend.module.chatting.dto;

import lombok.Getter;

@Getter
public class VideoSyncRequest {
  private String videoId;
  private Long currentTime;
  private Long playState;
}
