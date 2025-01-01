package com.zerobase.plistbackend.module.channel.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VideoResponse {

  private Long id;
  private String videoId;
  private String videoName;
  private String videoThumbnail;
  private String videoDescription;

}
