package com.zerobase.plistbackend.module.user.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
  ROLE_USER,
  ROLE_ADMIN,
  ROLE_NONE
}