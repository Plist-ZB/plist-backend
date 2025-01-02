package com.zerobase.plistbackend.module.user.type;

import lombok.Getter;

@Getter
public enum  RegistrationId {
  GOOGLE("Google"),
  KAKAO("Kakao");

  private final String id;

  RegistrationId(String id) {
    this.id = id;
  }

  public static RegistrationId fromId(String id) {
    for (RegistrationId registrationId : values()) {
      if (registrationId.id.equalsIgnoreCase(id)) {
        return registrationId;
      }
    }
    throw new IllegalArgumentException("Unknown RegistrationId: " + id);
  }

  @Override
  public String toString() {
    return this.id;
  }
}
