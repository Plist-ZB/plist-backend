package com.zerobase.plistbackend.module.home.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponse {

  private Long id;
  private String videoId;
  private String videoName;
  private String videoThumbnail;
  private String videoDescription;

}
