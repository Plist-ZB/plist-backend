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
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

  private final YouTubeApiProperties youTubeApiProperties;
  private final ObjectMapper objectMapper;

  @Override
  @Transactional
  public List<VideoResponse> searchVideo(String keyword) {
    List<VideoResponse> videoResponseList = new ArrayList<>();
    try {
      StringBuilder search = search(keyword);
      jsonParsingToVideoResponseList(videoResponseList, search);
    }catch (IOException e) {
      e.printStackTrace();
    }catch (ParseException e) {
      e.printStackTrace(); //TODO: try catch 문 어노테이션으로 변경.
    }
    return videoResponseList;
  }

  private StringBuilder search(String keyword) throws IOException {

    URL url = new URL(
        String.format(youTubeApiProperties.getUrl(), youTubeApiProperties.getApiKey(), keyword,
            youTubeApiProperties.getType(),
            youTubeApiProperties.getPart(), youTubeApiProperties.getOrder(),
            youTubeApiProperties.getRelevanceLanguage(),
            youTubeApiProperties.getVideoEmbeddable(),
            youTubeApiProperties.getMaxResults(),
            youTubeApiProperties.getTopicId()));

    BufferedReader br;

    br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

    StringBuilder sb = new StringBuilder();
    String inputLine;
    while ((inputLine = br.readLine()) != null) {
      sb.append(inputLine);
    }
    return sb;
  }

  private void jsonParsingToVideoResponseList (List<VideoResponse> videoResponseList, StringBuilder search)
      throws ParseException {
    String result = search.toString();

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
  }
}
