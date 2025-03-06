package com.zerobase.plistbackend.module.playlist.domain;

import com.zerobase.plistbackend.module.home.model.Video;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlaylistCrudEventPublisher {

  private final SimpMessageSendingOperations messagingTemplate;

  @EventListener
  public void handlePlaylistUpdateEvent(PlaylistCrudEvent event) {
    log.info("Call Playlist Update Event : {}", event.getChannel().getChannelId());
    Long destinationVariable = event.getChannel().getChannelId();
    List<Video> result = event.getChannel().getChannelPlaylist().getVideoList();

    messagingTemplate.convertAndSend("/sub/video." + destinationVariable, result);
  }
}
