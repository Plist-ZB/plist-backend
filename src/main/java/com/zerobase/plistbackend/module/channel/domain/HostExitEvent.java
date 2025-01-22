package com.zerobase.plistbackend.module.channel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HostExitEvent {

  private Long channelId;
}
