package com.zerobase.plistbackend.module.userplaylist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Video {

  private final String videoName;
  private final String videoThumbnail;
  private final String videoId;

}
