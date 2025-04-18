package com.zerobase.plistbackend.module.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.zerobase.plistbackend.module.fcm.dto.FCMTokenRequest;
import com.zerobase.plistbackend.module.fcm.entity.FCMToken;
import com.zerobase.plistbackend.module.fcm.exception.FCMTokenException;
import com.zerobase.plistbackend.module.fcm.repository.FCMTokenRepository;
import com.zerobase.plistbackend.module.fcm.type.FCMTokenErrorStatus;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMTokenServiceImpl implements FCMTokenService {

  private final FCMTokenRepository fcmTokenRepository;
  private final UserRepository userRepository;
  private final FirebaseMessaging firebaseMessaging;

  @Override
  @Transactional
  public void upsertFCMToken(CustomOAuth2User customOAuth2User, FCMTokenRequest token) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    FCMToken fcmToken = fcmTokenRepository.findByFcmTokenValue(token.getToken())
        .map(existsToken -> {
          existsToken.updateFCMToken();
          return existsToken;
        }).orElseGet(() -> FCMToken.from(token.getToken(), user));

    fcmTokenRepository.save(fcmToken);
  }

  @Override
  @Transactional
  public void deleteFCMToken(CustomOAuth2User customOAuth2User, FCMTokenRequest token) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    FCMToken fcmToken = fcmTokenRepository.findByFcmTokenValue(token.getToken()).orElseThrow(() -> new FCMTokenException(
        FCMTokenErrorStatus.NOT_FOUND));

    if (!fcmToken.getUser().equals(user)) {
      throw new FCMTokenException(FCMTokenErrorStatus.NOT_MY_TOKEN);
    }

    fcmTokenRepository.delete(fcmToken);
  }

  //TODO: 푸시 메시지 진행하면서 수정 필요.
  @Override
  public void sendPushMessage(String title, String body, String link,
      List<String> followersFCMTokenList) {

    if (followersFCMTokenList == null || followersFCMTokenList.isEmpty()) {
      return;
    }

    try {
      for (String token : followersFCMTokenList) {
        if (token == null || token.isBlank()) {
          continue;
        }

        Message message = Message.builder()
            .setWebpushConfig(WebpushConfig.builder()
                .putHeader("TTL", "1")
                .putData("title", title)
                .putData("body", body)
                .putData("link", link)
                .build())
            .setToken(token)
            .build();

//        Message message = Message.builder()
//            .setWebpushConfig(WebpushConfig.builder()
//                .setNotification(WebpushNotification.builder()
//                    .setTitle(title)
//                    .setBody(body)
//                    .setImage(
//                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1DNsfskjJtWhewcdCt8_4spNsZf7lmL3wKQ&s")
//                    .build())
//                .setFcmOptions(WebpushFcmOptions.withLink(link))
//                .build())
//            .setToken(token)
//            .putData("TTL", "1")
//            .build();

        firebaseMessaging.send(message);
      }
    } catch (FirebaseMessagingException e) {
      log.error("푸시알림 메시지 오류! ErrorCode : {}, ErrorMessage : {}", e.getErrorCode(),
          e.getMessage());
    }
  }
}