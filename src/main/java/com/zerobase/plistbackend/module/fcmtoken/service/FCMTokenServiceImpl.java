package com.zerobase.plistbackend.module.fcmtoken.service;

import com.zerobase.plistbackend.module.fcmtoken.entity.FCMToken;
import com.zerobase.plistbackend.module.fcmtoken.repository.FCMTokenRepository;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
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

  @Override
  @Transactional
  public void upsertFCMToken(CustomOAuth2User customOAuth2User, String token) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    FCMToken fcmToken = fcmTokenRepository.findByFcmTokenValue(token)
        .map(existsToken -> {
          existsToken.updateFCMToken();
          return existsToken;
        }).orElseGet(() -> FCMToken.from(token, user));

    fcmTokenRepository.save(fcmToken);
  }
}