package com.zerobase.plistbackend.module.userplaylist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserPlaylistRequest {
  @Schema(required = true, description = "유저 재생목록의 제목을 입력")
  private String userPlaylistName;
}
