package com.zerobase.plistbackend.module.subscribe.service;

import com.zerobase.plistbackend.module.subscribe.dto.response.SubscribeResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;

public interface SubscribeService {

  SubscribeResponse findFollowees(CustomOAuth2User customOAuth2User);

  void subcribe(CustomOAuth2User customOAuth2User, Long followeeId);

  void unsubscribe(CustomOAuth2User customOAuth2User, Long followeeId);
}