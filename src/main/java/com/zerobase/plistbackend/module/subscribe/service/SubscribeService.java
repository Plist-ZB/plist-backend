package com.zerobase.plistbackend.module.subscribe.service;

import com.zerobase.plistbackend.module.subscribe.dto.response.SubscribeResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;

public interface SubscribeService {

  SubscribeResponse findFollowers(CustomOAuth2User customOAuth2User);

  void subscribe(CustomOAuth2User customOAuth2User, Long followerId);

  void unsubscribe(CustomOAuth2User customOAuth2User, Long followerId);
}