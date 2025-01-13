package com.zerobase.plistbackend.module.channel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelRequest {

  @Schema(description = "채널 이름", maxLength = 50)
  @Size(max = 50, message = "채널이름은 최대 50자까지 작성할 수 있습니다.")
  private String channelName;

  @Schema(description = "채널에 추가할 내 플레이리스트 Id", nullable = true)
  private Long userPlaylistId;

  @Schema(description = "카테고리 Id", allowableValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9",
      "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"})
  private Long categoryId;

//  @Schema(description = "채널 썸네일", nullable = true)
//  private String channelThumbnail;
//  private Long channelCapacity;

}
