package com.zerobase.plistbackend.module.userplaylist.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoRequest {

  private String videoId;
  private String videoName;
  private String videoThumbnail;
}
