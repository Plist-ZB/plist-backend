package com.zerobase.plistbackend.module.playlist.domain;

import com.zerobase.plistbackend.module.channel.dto.response.DetailChannelResponse;
import com.zerobase.plistbackend.module.channel.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaylistCrudPublisher {

  private final ChannelService channelService;
  private final SimpMessagingTemplate messagingTemplate;

  @EventListener
  public void handlePlaylistUpdateEvent(PlaylistCrudEvent event) {
    Long destinationVariable = event.getChannelId();
    DetailChannelResponse result = channelService.findOneChannel(destinationVariable);
    messagingTemplate.convertAndSend("/sub/playlist." + destinationVariable, result.getVideoList());
  }
}
