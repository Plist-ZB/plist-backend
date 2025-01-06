package com.zerobase.plistbackend.module.channel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelRequest {

  private String channelName;
  private Long userPlaylistId;
  private String channelCategory;
  private String channelThumbnail;
  private Long channelCapacity;

}
