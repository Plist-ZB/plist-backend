package com.zerobase.plistbackend.module.playlist.domain;

import com.zerobase.plistbackend.module.channel.dto.response.DetailChannelResponse;
import com.zerobase.plistbackend.module.channel.service.ChannelService;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
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

  @Operation(
      summary = "채널의 플레이리스트에서 변경 감지 시 작동, 채널에 속하는 플레이리스트의 최신 DB정보를 가져와 참여자들에게 전달",
      description = "Subscribe Path = {/sub/playlist.{channelId}}"
  )
  @EventListener
  public void handlePlaylistUpdateEvent(PlaylistCrudEvent event) {
    log.info("Call Playlist Update Event : {}", event.getChannelId());
    Long destinationVariable = event.getChannelId();
    CustomOAuth2User customOAuth2User = event.getCustomOAuth2User();
    DetailChannelResponse result = channelService.findOneChannel(destinationVariable, customOAuth2User);
    messagingTemplate.convertAndSend("/sub/playlist." + destinationVariable, result.getVideoList());
  }
}
