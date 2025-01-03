package com.zerobase.plistbackend.module.user.model.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetail {

  private String role;
  private String name;
  private String email;
}
