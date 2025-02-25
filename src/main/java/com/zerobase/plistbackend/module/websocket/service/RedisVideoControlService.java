package com.zerobase.plistbackend.module.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.websocket.domain.VideoSyncManager;
import com.zerobase.plistbackend.module.websocket.dto.response.VideoControlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisVideoControlService implements MessageListener {
    private final ObjectMapper mapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final VideoSyncManager videoSyncManager;
    private final SimpMessageSendingOperations messagingTemplate;

    public void publish(String channelId, String data) {
        redisTemplate.convertAndSend(channelId, data);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());
        try {
            VideoControlResponse response = mapper.readValue(body, VideoControlResponse.class);
            Long currentTime = videoSyncManager.getCurrentTime(response.getChannelId());
            messagingTemplate.convertAndSend("/sub/video." + response.getChannelId(), response);
            log.info("호스트가 채널 {}의 비디오 상태를 업데이트: 현재 시간={}", response.getChannelId(), currentTime);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
