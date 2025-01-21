package com.zerobase.plistbackend.module.websocket.domain;

import com.zerobase.plistbackend.module.websocket.dto.response.VideoSyncResponse;
import com.zerobase.plistbackend.module.websocket.dto.videointerface.VideoResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

@Getter
public class VideoResponseJsonMap {

  private final Map<String, VideoResponse> videoDataResponseMap = new ConcurrentHashMap<>();

  public void setUpData(String type,VideoSyncResponse response) {
    videoDataResponseMap.put(type, response);
  }

}
