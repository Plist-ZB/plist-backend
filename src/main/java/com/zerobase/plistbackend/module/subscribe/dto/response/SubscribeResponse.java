package com.zerobase.plistbackend.module.subscribe.dto.response;

import com.zerobase.plistbackend.module.user.dto.response.UserInfoResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscribeResponse {

  private Long userId;

  private List<UserInfoResponse> followees;

  public static SubscribeResponse of(Long userId, List<UserInfoResponse> followees) {
    return SubscribeResponse.builder()
        .userId(userId)
        .followees(followees)
        .build();
  }
}