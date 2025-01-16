package com.zerobase.plistbackend.module.websocket.service;

import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;

public interface WebSocketService {

  ChatMessageResponse sendMessage(ChatMessageRequest request);

  boolean isHost(Long channelId, CustomOAuth2User user);
}
