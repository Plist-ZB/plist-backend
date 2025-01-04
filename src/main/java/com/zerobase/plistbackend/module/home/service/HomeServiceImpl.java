package com.zerobase.plistbackend.module.home.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.common.app.config.YouTubeApiProperties;
import com.zerobase.plistbackend.module.home.dto.response.VideoResponse;
import com.zerobase.plistbackend.module.userplaylist.domain.Video;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

  private final YouTubeApiProperties youTubeApiProperties;
  private final ObjectMapper objectMapper;

  @Override
  public List<VideoResponse> searchVideo(String searchValue) {

    List<VideoResponse> videoResponseList = new ArrayList<>();
    String result;

    try {
      StringBuilder sb = makeApiUrl(searchValue);
      result = sb.toString();

      JSONParser jsonParser = new JSONParser();
      JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
      JSONArray items = (JSONArray) jsonObject.get("items");

      for (int i = 0; i < items.size(); i++) {
        JSONObject item = (JSONObject) items.get(i);
        JSONObject id = (JSONObject) item.get("id");
        JSONObject snippet = (JSONObject) item.get("snippet");
        JSONObject thumbnails = (JSONObject) snippet.get("thumbnails");
        JSONObject thumbnail_default = (JSONObject) thumbnails.get("default");

        VideoResponse videoResponse = VideoResponse.builder()
            .id((long) i)
            .videoId((String) id.get("videoId"))
            .videoName((String) snippet.get("title"))
            .videoThumbnail((String) thumbnail_default.get("url"))
            .videoDescription((String) snippet.get("description"))
            .build();

        videoResponseList.add(videoResponse);
      }
    } catch (Exception e) {
      e.printStackTrace(); // TODO : 예외처리
    }

    return videoResponseList;
  }

  @Override
  public Video getVideo(String videoId) throws IOException {
    return youTubeApiProperties.createVideo(videoId, objectMapper);
  }


  private StringBuilder makeApiUrl(String searchValue) throws IOException {

    URL url = new URL(
        String.format(youTubeApiProperties.getUrl(), youTubeApiProperties.getApiKey(), searchValue,
            youTubeApiProperties.getType(),
            youTubeApiProperties.getPart(), youTubeApiProperties.getMaxResults(),
            youTubeApiProperties.getOrder(), youTubeApiProperties.getRelevanceLanguage(),
            youTubeApiProperties.getVideoEmbeddable()));

    BufferedReader br;

    br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

    StringBuilder sb = new StringBuilder();
    String inputLine;
    while ((inputLine = br.readLine()) != null) {
      sb.append(inputLine);
    }
    return sb;
  }

}
