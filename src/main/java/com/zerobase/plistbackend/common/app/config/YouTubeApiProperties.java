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
  private final String selectVideoUrl;
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
    this.selectVideoUrl = selectVideoUrl;
    this.type = type;
    this.part = part;
    this.maxResults = maxResults;
    this.order = order;
    this.relevanceLanguage = relevanceLanguage;
    this.videoEmbeddable = videoEmbeddable;
    this.topicId = topicId;
    this.apiKey = apiKey;
  }

  @IOExceptionHandler
  public Video createVideo(String videoId, ObjectMapper objectMapper)
      throws IOException {
    String videoAsString = getVideoAsString(videoId, objectMapper);
    return getVideo(objectMapper, videoAsString);
  }

  private String getVideoAsString(String videoId, ObjectMapper objectMapper) throws IOException {
    URL selectOneQueryURL = new URL(String.format(this.selectVideoUrl, this.getApiKey(), videoId));

    BufferedReader br = new BufferedReader(
        new InputStreamReader(selectOneQueryURL.openStream(), StandardCharsets.UTF_8));

    StringBuilder sb = new StringBuilder();

    String inputLine;
    while ((inputLine = br.readLine()) != null) {
      sb.append(inputLine);
    }

    return objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(objectMapper.readTree(sb.toString()));
  }

  private Video getVideo(ObjectMapper objectMapper, String videoAsString)
      throws JsonProcessingException {
    final Long selectOneId = 1L;

    JsonNode rootNode = objectMapper.readTree(videoAsString);
    JsonNode itemsNode = rootNode.get("items");

    Video video = new Video();
    for (JsonNode itemNode : itemsNode) {
      JsonNode idNode = itemNode.get("id");
      JsonNode snippetNode = itemNode.get("snippet");
      JsonNode thumbnailsNode = snippetNode.get("thumbnails");
      JsonNode thumbnailDefaultNode = thumbnailsNode.get("default");

      video = Video.builder()
          .videoId(idNode.asText())
          .videoName(snippetNode.get("title").asText())
          .videoThumbnail(thumbnailDefaultNode.get("url").asText())
          .build();

    }
    return video;
  }
}
