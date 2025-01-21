package com.zerobase.plistbackend.module.playlist.domain;

import com.zerobase.plistbackend.module.channel.dto.response.DetailChannelResponse;
import com.zerobase.plistbackend.module.channel.service.ChannelService;
import com.zerobase.plistbackend.module.home.model.Video;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
  private static final String PLAYLIST = "playlist";
  private final Map<String, List<Video>> videoMap = new ConcurrentHashMap<>();

  @EventListener
  public void handlePlaylistUpdateEvent(PlaylistCrudEvent event) {
    log.info("Call Playlist Update Event : {}", event.getChannelId());
    Long destinationVariable = event.getChannelId();
    CustomOAuth2User customOAuth2User = event.getCustomOAuth2User();
    DetailChannelResponse result = channelService.findOneChannel(destinationVariable, customOAuth2User);
    videoMap.put(PLAYLIST,result.getVideoList());
    log.info("videoMap = {}", videoMap);
    messagingTemplate.convertAndSend("/sub/video." + destinationVariable, videoMap);
  }
}
