package com.zerobase.plistbackend.module.fcmtoken.service;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;

public interface FCMTokenService {

  void upsertFCMToken(CustomOAuth2User customOAuth2User, String token);
}