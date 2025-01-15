package com.zerobase.plistbackend.module.websocket.service.mock;

import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.websocket.service.WebSocketService;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;

public class TestWebSocketService implements WebSocketService {

  @Override
  public ChatMessageResponse sendMessage(ChatMessageRequest request) {
    return ChatMessageResponse.from(request, "TestImg.img");
  }

  @Override
  public boolean isHost(Long channelId, CustomOAuth2User user, ChannelStatus channelStatusActive) {
    return false;
  }
}
