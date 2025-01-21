package com.zerobase.plistbackend.module.websocket.domain;

import com.zerobase.plistbackend.module.websocket.dto.response.VideoSyncResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class VideoResponseJsonMap {

  private final Map<String, VideoSyncResponse> videoSyncResponseMap = new ConcurrentHashMap<>();
  private static final String TYPE = "videoState";

  public void setUpData(VideoSyncResponse response) {
    videoSyncResponseMap.put(TYPE, response);
  }

}
