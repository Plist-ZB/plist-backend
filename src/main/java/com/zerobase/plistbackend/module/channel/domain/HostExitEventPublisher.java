package com.zerobase.plistbackend.module.channel.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HostExitEventPublisher {

  private final SimpMessagingTemplate messagingTemplate;

  @EventListener
  public void handleHostExitEvent(HostExitEvent event) {
    log.info("Call HostExit Event : {}", event.getChannelId());
    Long destinationVariable = event.getChannelId();
    String message = "CHANNEL_CLOSED";

    messagingTemplate.convertAndSend("/sub/exit." + destinationVariable, message);
  }
}
