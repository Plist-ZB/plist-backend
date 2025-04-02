package com.zerobase.plistbackend.module.message.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UnreadResponse {

  private boolean unread;

  public static UnreadResponse of(boolean result) {
    return UnreadResponse.builder()
        .unread(result)
        .build();
  }
}
