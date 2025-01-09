package com.zerobase.plistbackend.module.chatting.domain;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class VideoSyncManager {

  private static final ConcurrentHashMap<Long, Long> videoSyncDataMap = new ConcurrentHashMap<>();

  public void updateCurrentTime(Long channelId, Long currentTime) {
    videoSyncDataMap.put(channelId, currentTime);
  }

  public Long getCurrentTime(Long channelId) {
    return videoSyncDataMap.getOrDefault(channelId, 0L);
  }
}
