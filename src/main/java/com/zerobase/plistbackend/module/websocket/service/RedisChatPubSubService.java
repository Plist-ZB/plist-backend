package com.zerobase.plistbackend.module.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisChatPubSubService implements MessageListener {
    private final ObjectMapper mapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate<String,String> redisTemplate;

    public void publish(String channelId, String message) {
        redisTemplate.convertAndSend(channelId, message);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());
        try {
            ChatMessageResponse response = mapper.readValue(body, ChatMessageResponse.class);
            messagingTemplate.convertAndSend("/sub/chat." + response.getChannelId(), response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
