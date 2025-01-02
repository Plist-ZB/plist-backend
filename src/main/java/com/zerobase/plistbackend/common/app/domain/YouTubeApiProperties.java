package com.zerobase.plistbackend.common.app.domain;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("youtube-api")
public class YouTubeApiProperties {
  private final String url;
  private final String type;
  private final String part;
  private final long maxResults;
  private final String order;
  private final String relevanceLanguage;
  private final String videoEmbeddable;
  private final String apiKey;

  public YouTubeApiProperties(String url, String type, String part, long maxResults, String order,
      String relevanceLanguage, String videoEmbeddable, String apiKey) {
    this.url = url;
    this.type = type;
    this.part = part;
    this.maxResults = maxResults;
    this.order = order;
    this.relevanceLanguage = relevanceLanguage;
    this.videoEmbeddable = videoEmbeddable;
    this.apiKey = apiKey;
  }
}
