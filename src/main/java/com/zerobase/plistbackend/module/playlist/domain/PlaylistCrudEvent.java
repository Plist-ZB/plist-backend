package com.zerobase.plistbackend.module.playlist.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaylistCrudEvent {
  private Long channelId;
}
