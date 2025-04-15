package com.zerobase.plistbackend.module.channel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtherClosedChannelResponse {

  private final Long channelId;
  private final String channelName;
  private final String channelCategoryName;
  private final String channelThumbnail;
  private final String channelCreatedAt;

  public OtherClosedChannelResponse(ClosedChannelResponse closedChannelResponse) {
    this.channelId = closedChannelResponse.getChannelId();
    this.channelName = closedChannelResponse.getChannelName();
    this.channelCategoryName = closedChannelResponse.getChannelName();
    this.channelThumbnail = closedChannelResponse.getChannelThumbnail();
    this.channelCreatedAt = closedChannelResponse.getChannelCreatedAt();
  }

}
