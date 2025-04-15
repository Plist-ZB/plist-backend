package com.zerobase.plistbackend.module.subscribe.domain;

import com.zerobase.plistbackend.module.fcm.entity.FCMToken;
import com.zerobase.plistbackend.module.fcm.repository.FCMTokenRepository;
import com.zerobase.plistbackend.module.fcm.service.FCMTokenService;
import com.zerobase.plistbackend.module.message.entity.Message;
import com.zerobase.plistbackend.module.message.repository.MessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscribeEventPublisher {

  private final FCMTokenService fcmTokenService;
  private final MessageRepository messageRepository;
  private final FCMTokenRepository fcmTokenRepository;
  @Value("${login-url}")
  private String loginUrl;

  @EventListener
  public void handleSubscribeEvent(SubscribeEvent event) {

    List<FCMToken> followeeFCMtokenList = fcmTokenRepository.findByUser(event.getUser());

    String title = "플리스트 알림";
    String body = event.getFollowerName() + "님이 회원님을 구독했습니다.";
    String link = loginUrl + "/user/profile/" + event.getFollowerId();

    if (!followeeFCMtokenList.isEmpty()) {
      List<String> token = followeeFCMtokenList.stream().map(FCMToken::getFcmTokenValue).toList();

      fcmTokenService.sendPushMessage(title, body, link, token);
    }

    Message message = Message.from(event.getUser(), body, link);
    messageRepository.save(message);
  }
}
