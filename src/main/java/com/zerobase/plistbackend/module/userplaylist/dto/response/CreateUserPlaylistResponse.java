package com.zerobase.plistbackend.module.userplaylist.dto.response;

import lombok.Getter;

@Getter
public class CreateUserPlaylistResponse {
  private final String result;

  public CreateUserPlaylistResponse(String title) {
    this.result = title + " 재생목록을 만들었습니다";
  }
}
