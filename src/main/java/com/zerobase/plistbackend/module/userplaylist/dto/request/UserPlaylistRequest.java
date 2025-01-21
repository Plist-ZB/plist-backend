package com.zerobase.plistbackend.module.userplaylist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPlaylistRequest {
  @Schema(description = "유저 재생목록의 제목을 입력")
  @Size(min = 1, max = 50, message = "내 플레이리스트 제목은 최대 50자까지 입력할 수 있습니다.")
  private String userPlaylistName;
}
