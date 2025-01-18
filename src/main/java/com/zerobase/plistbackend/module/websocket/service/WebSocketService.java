package com.zerobase.plistbackend.module.websocket.service;

import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;

public interface WebSocketService {

  ChatMessageResponse sendMessage(ChatMessageRequest request);

  boolean isHost(Long channelId);
}
