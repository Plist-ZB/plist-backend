package com.zerobase.plistbackend.module.websocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoControlRequest {
  private String email;
  private String videoId;
  private Long currentTime;
  private Long playState;
}
