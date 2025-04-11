package com.zerobase.plistbackend.module.user.dto.response;

import com.zerobase.plistbackend.module.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {

  private final String email;
  private final String nickname;
  private final String image;
  private final int followers;


  public static ProfileResponse createProfileResponse(User user, int followers) {
    return new ProfileResponse(user.getUserEmail(), user.getUserName(), user.getUserImage(),
        followers);
  }
}