package com.zerobase.plistbackend.common.app.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
