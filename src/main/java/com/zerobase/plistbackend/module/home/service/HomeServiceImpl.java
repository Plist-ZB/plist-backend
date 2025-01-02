package com.zerobase.plistbackend.module.home.service;

import com.zerobase.plistbackend.common.app.config.YoutubeDataApiKey;
import com.zerobase.plistbackend.module.home.dto.response.VideoResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

  private final YoutubeDataApiKey youtubeDataApiKey;

  @Override
  @Transactional
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

  private StringBuilder makeApiUrl(String searchValue) throws IOException {
    String GOOGLE_API_URL = "https://www.googleapis.com/youtube/v3/search?key=%s&q=%s&type=%s&part=%s&maxResults=%d&order=%s&relevanceLanguage=%s&videoEmbeddable=%s";
    // 검색할 타입
    String TYPE = "video";
    // 응답에 포함될 내용
    String PART = "snippet,id";
    // 페이지 당 보여주는 응답 갯수 범위 0~50
    int MAX_RESULTS = 50;
    // 조회수가 많은 순서대로 정렬 (others : date 업로드 날짜순, rating 평점순, relevance 가장 검색어에 가까운 순(default), title (제목 알파벳 순))
    String ORDER = "viewCount";
    // 가장 관련있는 언어로 검색, 우선 한국어가 우선 이후 관련된 언어도 같이 검색
    String RELEVANCE_LANGUAGE = "ko";
    String VIDEO_EMBEDDABLE = "true";

    URL url = new URL(
        String.format(GOOGLE_API_URL, youtubeDataApiKey.getYoutubeDataApiKey(), searchValue, TYPE,
            PART, MAX_RESULTS,
            ORDER, RELEVANCE_LANGUAGE, VIDEO_EMBEDDABLE));

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
