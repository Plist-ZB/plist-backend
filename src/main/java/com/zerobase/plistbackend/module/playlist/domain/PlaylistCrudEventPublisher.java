package com.zerobase.plistbackend.module.playlist.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.channel.dto.response.DetailChannelResponse;
import com.zerobase.plistbackend.module.channel.service.ChannelService;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
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
  private final ObjectMapper mapper;
  private final Map<String, String> videoMap = new ConcurrentHashMap<>();

  @EventListener
  public void handlePlaylistUpdateEvent(PlaylistCrudEvent event) throws JsonProcessingException {
    log.info("Call Playlist Update Event : {}", event.getChannelId());
    Long destinationVariable = event.getChannelId();
    CustomOAuth2User customOAuth2User = event.getCustomOAuth2User();
    DetailChannelResponse result = channelService.findOneChannel(destinationVariable, customOAuth2User);

    String type = mapper.writeValueAsString(PLAYLIST);
    String videoList = mapper.writeValueAsString(result.getVideoList());
    videoMap.put(type,videoList);


    messagingTemplate.convertAndSend("/sub/video." + destinationVariable, videoMap);
  }
}
