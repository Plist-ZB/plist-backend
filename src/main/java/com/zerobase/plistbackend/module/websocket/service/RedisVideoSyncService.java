package com.zerobase.plistbackend.module.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.websocket.domain.VideoSyncManager;
import com.zerobase.plistbackend.module.websocket.dto.response.VideoSyncResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisVideoSyncService implements MessageListener {
    private final ObjectMapper mapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final VideoSyncManager videoSyncManager;


    public void publish(String channelId, String videoSyncData) {
        redisTemplate.convertAndSend(channelId, videoSyncData);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String syncData = new String(message.getBody());
        try {
            VideoSyncResponse response = mapper.readValue(syncData, VideoSyncResponse.class);
            videoSyncManager.updateCurrentTime(response.getChannelId(),response.getCurrentTime());
            messagingTemplate.convertAndSend("/sub/video." + response.getChannelId(), response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
