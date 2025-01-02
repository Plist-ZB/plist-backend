package com.zerobase.plistbackend.common.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class YoutubeDataApiKey {

  @Value("${youtube_data_api_key}")
  private String youtubeDataApiKey;
}
