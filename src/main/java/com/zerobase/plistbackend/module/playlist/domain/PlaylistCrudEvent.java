package com.zerobase.plistbackend.module.playlist.domain;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaylistCrudEvent {

  private Channel channel;
}
