package com.zerobase.plistbackend.module.channel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelEvent {

  public enum EventType { CREATED, CLOSED }

  private Long hostId;

  private String hostName;

  private Long channelId;

  private EventType eventType;
}
