package com.zerobase.plistbackend.module.message.service;

import com.zerobase.plistbackend.module.message.dto.response.MessageResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import java.util.List;

public interface MessageService {

  List<MessageResponse> findMessages(CustomOAuth2User customOAuth2User);

  void readMessage(CustomOAuth2User customOAuth2User, Long messageId);

  void readAllMessage(CustomOAuth2User customOAuth2User);
}
