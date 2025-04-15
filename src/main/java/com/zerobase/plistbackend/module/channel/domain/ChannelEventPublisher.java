package com.zerobase.plistbackend.module.channel.domain;

import com.zerobase.plistbackend.module.channel.domain.ChannelEvent.EventType;
import com.zerobase.plistbackend.module.fcm.service.FCMTokenService;
import com.zerobase.plistbackend.module.message.repository.MessageBatchRepository;
import com.zerobase.plistbackend.module.subscribe.repository.FollowerRepository;
import com.zerobase.plistbackend.module.subscribe.repository.SubscribeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelEventPublisher {

  private final FCMTokenService fcmTokenService;
  private final FollowerRepository followerRepository;
  private final SubscribeRepository subscribeRepository;
  private final MessageBatchRepository messageBatchRepository;

  @Value("${login-url}")
  private String loginUrl;

  @EventListener
  public void handleChannelEvent(ChannelEvent event) {
    if (event.getEventType() == EventType.CREATED) {
      handleChannelCreatedEvent(event);
    } else if (event.getEventType() == EventType.CLOSED) {
      handleChannelClosedEvent(event);
    }
  }

  private void handleChannelCreatedEvent(ChannelEvent event) {

    List<String> followersFCMTokenList = followerRepository.findFollowersFCMtokenByFolloweeId(
        event.getHostId());
    List<Long> followersIds = subscribeRepository.findFollowersIdByFolloweeId(event.getHostId());

    String title = "플리스트 알림";
    String body = event.getHostName() + "님이 라이브 방송을 시작했습니다.";
    String link = loginUrl + "/channel/" + event.getChannelId();

    if (!followersFCMTokenList.isEmpty()) {
      fcmTokenService.sendPushMessage(title, body, link, followersFCMTokenList);
    }
    if (!followersIds.isEmpty()) {
      messageBatchRepository.batchInsertMessages(followersIds, body, link);
    }
  }

  private void handleChannelClosedEvent(ChannelEvent event) {

    List<Long> followersIds = subscribeRepository.findFollowersIdByFolloweeId(event.getHostId());

    if (!followersIds.isEmpty()) {
      messageBatchRepository.batchDeleteMessages(followersIds);
    }
  }
}
