package com.zerobase.plistbackend.module.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

  private Long userId;

  private String userName;
}