package com.zerobase.plistbackend.module.chatting.service;

import com.zerobase.plistbackend.module.chatting.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.chatting.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;

public interface ChatService {

  ChatMessageResponse sendMessage(ChatMessageRequest request);

  boolean isHost(CustomOAuth2User user);
}
