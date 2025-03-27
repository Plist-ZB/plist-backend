package com.zerobase.plistbackend.module.subscribe.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscribeResponse {

  private List<FolloweeInfoResponse> followees;

  public static SubscribeResponse of(List<FolloweeInfoResponse> followees) {
    return SubscribeResponse.builder()
        .followees(followees)
        .build();
  }
}