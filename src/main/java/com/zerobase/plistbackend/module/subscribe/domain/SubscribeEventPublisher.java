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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscribeEventPublisher {

  @Value("${login-url}")
  private String loginUrl;

  private final FCMTokenService fcmTokenService;
  private final MessageRepository messageRepository;
  private final FCMTokenRepository fcmTokenRepository;

  @Async
  @TransactionalEventListener
  public void handleSubscribeEvent(SubscribeEvent event) {

    FCMToken followeeFCMtoken = fcmTokenRepository.findByUser(event.getUser());
    List<String> token = List.of(followeeFCMtoken.getFcmTokenValue());

    String title = "플리스트 알림";
    String body = event.getFollowerName() + "님이 회원님을 구독했습니다.";
    String link = "";//TODO: 다른 사용자 조회 API로 추가 필요

    fcmTokenService.sendPushMessage(title, body, link, token);

    Message message = Message.from(event.getUser(), body, link);
    messageRepository.save(message);
  }
}
