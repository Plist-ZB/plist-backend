package com.zerobase.plistbackend.module.subscribe.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscribeResponse {

  private List<FollowerInfoResponse> followers;

  public static SubscribeResponse of(List<FollowerInfoResponse> followers) {
    return SubscribeResponse.builder()
        .followers(followers)
        .build();
  }
}