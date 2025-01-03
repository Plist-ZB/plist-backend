package com.zerobase.plistbackend.common.app.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.common.app.aop.TryCatch;
import com.zerobase.plistbackend.module.home.dto.response.VideoResponse;
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
  private final long maxResults;
  private final String order;
  private final String relevanceLanguage;
  private final String videoEmbeddable;
  private final String apiKey;

  public YouTubeApiProperties(String url, String selectVideoUrl, String type, String part,
      long maxResults, String order, String relevanceLanguage, String videoEmbeddable,
      String apiKey) {
    this.url = url;
    this.selectVideoUrl = selectVideoUrl;
    this.type = type;
    this.part = part;
    this.maxResults = maxResults;
    this.order = order;
    this.relevanceLanguage = relevanceLanguage;
    this.videoEmbeddable = videoEmbeddable;
    this.apiKey = apiKey;
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

  @TryCatch
  public VideoResponse createVideoResponse(String videoId, ObjectMapper objectMapper)
      throws IOException {
    String videoAsString = getVideoAsString(videoId, objectMapper);

    JsonNode rootNode = objectMapper.readTree(videoAsString);
    JsonNode itemsNode = rootNode.get("items");

    VideoResponse response = new VideoResponse();
    for (JsonNode itemNode : itemsNode) {
      JsonNode idNode = itemNode.get("id");
      JsonNode snippetNode = itemNode.get("snippet");
      JsonNode thumbnailsNode = snippetNode.get("thumbnails");
      JsonNode thumbnailDefaultNode = thumbnailsNode.get("default");

      response = VideoResponse.builder()
          .videoId(idNode.asText())
          .videoName(snippetNode.get("title").asText())
          .videoThumbnail(thumbnailDefaultNode.get("url").asText())
          .videoDescription(snippetNode.get("description").asText())
          .build();
    }
    return response;
  }
}
