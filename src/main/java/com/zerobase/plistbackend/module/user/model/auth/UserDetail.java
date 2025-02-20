package com.zerobase.plistbackend.module.user.model.auth;

import com.zerobase.plistbackend.module.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetail {

  private Long id;
  private String role;
  private String name;
  private String email;
  private Boolean isMember;

  public static UserDetail from(User user, Boolean isMember) {
    return UserDetail.builder()
        .id(user.getUserId())
        .name(user.getUserName())
        .email(user.getUserEmail())
        .role(String.valueOf(user.getUserRole()))
        .isMember(isMember).build();
  }
}
