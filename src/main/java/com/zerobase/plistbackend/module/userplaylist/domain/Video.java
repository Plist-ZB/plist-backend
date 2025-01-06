package com.zerobase.plistbackend.module.userplaylist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {

  private String videoName;
  private String videoThumbnail;
  private String videoId;

}

