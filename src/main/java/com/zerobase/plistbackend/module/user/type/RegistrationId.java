package com.zerobase.plistbackend.module.user.type;

import com.zerobase.plistbackend.module.user.dto.response.GoogleResponse;
import com.zerobase.plistbackend.module.user.dto.response.KakaoResponse;
import com.zerobase.plistbackend.module.user.dto.response.NaverResponse;
import com.zerobase.plistbackend.module.user.dto.response.OAuth2Response;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public enum  RegistrationId {
  GOOGLE("Google"),
  KAKAO("Kakao"),
  NAVER("Naver");

  private final String id;

  RegistrationId(String id) {
    this.id = id;
  }

  public static OAuth2Response fromId(String id, OAuth2User oAuth2User) {
    if (id.equalsIgnoreCase(GOOGLE.id)) {
      return new GoogleResponse(oAuth2User.getAttributes());
    }
    if (id.equalsIgnoreCase(NAVER.id)) {
      return new NaverResponse(oAuth2User.getAttributes());
    }
    if (id.equalsIgnoreCase(KAKAO.id)) {
      return new KakaoResponse(oAuth2User.getAttributes());
    }
    throw new IllegalArgumentException("Unknown RegistrationId: " + id);
  }

  @Override
  public String toString() {
    return this.id;
  }
}
