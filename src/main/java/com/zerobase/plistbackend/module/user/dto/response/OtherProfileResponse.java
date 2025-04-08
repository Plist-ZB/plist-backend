package com.zerobase.plistbackend.module.user.dto.response;

import com.zerobase.plistbackend.module.user.entity.User;

public record OtherProfileResponse(
    Long userId,
    String userName,
    String image,
    int followers,
    boolean isSubscribe,
    boolean isLive) {

  public static OtherProfileResponse createOtherProfileResponse(
      User user, int followers, boolean isSubscribe, boolean isLive) {

    return new OtherProfileResponse(
        user.getUserId(),
        user.getUserName(),
        user.getUserImage(),
        followers,
        isSubscribe,
        isLive
    );
  }

}
