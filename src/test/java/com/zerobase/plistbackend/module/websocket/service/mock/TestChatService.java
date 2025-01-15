package com.zerobase.plistbackend.module.websocket.service.mock;

import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.websocket.service.ChatService;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;

public class TestChatService implements ChatService {

  @Override
  public ChatMessageResponse sendMessage(ChatMessageRequest request) {
    return ChatMessageResponse.from(request, "TestImg.img");
  }

  @Override
  public boolean isHost(CustomOAuth2User user) {
    return false;
  }
}
