package com.zerobase.plistbackend.module.fcm.service;

import com.zerobase.plistbackend.module.fcm.dto.FCMTokenRequest;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import java.util.List;

public interface FCMTokenService {

  void upsertFCMToken(CustomOAuth2User customOAuth2User, FCMTokenRequest token);

  void sendPushMessage(String title, String body, String link, List<String> followersFCMTokenList);

  void deleteFCMToken(CustomOAuth2User customOAuth2User, FCMTokenRequest token);
}