package com.zerobase.plistbackend.module.userplaylist.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoRequest {

  @JsonProperty("videoId")
  private String videoId;
  @JsonProperty("videoName")
  private String videoName;
  @JsonProperty("videoThumbnail")
  private String videoThumbnail;
  @JsonProperty("videoDescription")
  private String videoDescription;
}
