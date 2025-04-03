package com.zerobase.plistbackend.module.fcm.service;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import java.util.List;

public interface FCMTokenService {

  void upsertFCMToken(CustomOAuth2User customOAuth2User, String token);

  void sendPushMessage(String title, String body, String link, List<String> followersFCMTokenList);
}