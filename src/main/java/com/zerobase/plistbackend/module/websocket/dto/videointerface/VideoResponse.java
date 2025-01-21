package com.zerobase.plistbackend.module.websocket.dto.videointerface;

public interface VideoResponse {
  String getVideoId();
  Long getPlayStates();
  Long getCurrentTime();
}
