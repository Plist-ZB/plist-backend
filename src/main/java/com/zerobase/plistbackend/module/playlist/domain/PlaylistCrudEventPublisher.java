package com.zerobase.plistbackend.module.playlist.domain;

import com.zerobase.plistbackend.module.channel.dto.response.DetailChannelResponse;
import com.zerobase.plistbackend.module.channel.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlaylistCrudEventPublisher {

  private final ChannelService channelService;
  private final SimpMessagingTemplate messagingTemplate;

  @EventListener
  public void handlePlaylistUpdateEvent(PlaylistCrudEvent event) {
    log.info("Received playlist update event: {}", event.getChannelId());
    Long destinationVariable = event.getChannelId();
    DetailChannelResponse result = channelService.findOneChannel(destinationVariable);
    messagingTemplate.convertAndSend("/sub/playlist." + destinationVariable, result.getVideoList());
  }
}
