package com.zerobase.plistbackend.module.user.dto.response;

public interface OAuth2Response {

  // 제공자
  String findProvider();

  // 아이디
  String findProviderId();

  // 이메일
  String findEmail();

  // 사용자 이름
  String findName();

  // 사용자 이미지
  String findImage();
}
