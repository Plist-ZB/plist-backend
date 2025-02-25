package com.zerobase.plistbackend.module.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.websocket.dto.request.NewUserWelcomeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisNewUserEnterService implements MessageListener {
    private final ObjectMapper mapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String channelId, String message) {
        redisTemplate.convertAndSend(channelId, message);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());
        try {
            NewUserWelcomeMessage welcomeMessage = mapper.readValue(body, NewUserWelcomeMessage.class);
            messagingTemplate.convertAndSend("/sub/enter." + welcomeMessage.getChannelId(), welcomeMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
