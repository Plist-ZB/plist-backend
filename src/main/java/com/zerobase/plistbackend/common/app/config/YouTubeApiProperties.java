package com.zerobase.plistbackend.common.app.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.common.app.aop.IOExceptionHandler;
import com.zerobase.plistbackend.module.userplaylist.domain.Video;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Getter
@ConfigurationProperties("youtube-api")
public class YouTubeApiProperties {

  private final String url;
  private final String type;
  private final String part;
  private final int maxResults;
  private final String order;
  private final String relevanceLanguage;
  private final String videoEmbeddable;
  private final String topicId;
  private final String apiKey;

  public YouTubeApiProperties(String url, String type, String part,
      int maxResults, String order, String relevanceLanguage,
      String videoEmbeddable, String topicId, String apiKey) {
    this.url = url;
    this.type = type;
    this.part = part;
    this.maxResults = maxResults;
    this.order = order;
    this.relevanceLanguage = relevanceLanguage;
    this.videoEmbeddable = videoEmbeddable;
    this.topicId = topicId;
    this.apiKey = apiKey;
  }
}
