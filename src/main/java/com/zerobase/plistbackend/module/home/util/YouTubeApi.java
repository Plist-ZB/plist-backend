package com.zerobase.plistbackend.module.home.util;

import com.zerobase.plistbackend.common.app.aop.IOExceptionHandler;
import com.zerobase.plistbackend.module.home.model.Video;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Getter
@ConfigurationProperties("youtube-api")
public class YouTubeApi {

  private final String url;
  private final String type;
  private final String part;
  private final int maxResults;
  private final String order;
  private final String relevanceLanguage;
  private final String videoEmbeddable;
  private final String topicId;
  private final String apiKey;

  public YouTubeApi(String url, String type, String part,
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

  @IOExceptionHandler
  public List<Video> createVideoList(String keyword) throws IOException, ParseException {
    keyword = parseKeyword(keyword);
    List<Video> videoList = new ArrayList<>();
    BufferedReader bufferedReader = search(keyword);
    jsonParsingToVideoList(videoList, bufferedReader);

    return videoList;
  }

  private String parseKeyword(String keyword) {
    if (keyword.contains("www.youtube.com")) {
      String[] splitKeyword = keyword.split("v=");
      keyword = splitKeyword[splitKeyword.length - 1];
    } else if (keyword.contains("youtu.be")) {
      String[] splitKeyword = keyword.split("youtu\\.be/");
      keyword = splitKeyword[splitKeyword.length - 1].substring(0, 11);
    } else if (keyword.length() != 11) {
      keyword = keyword + "%20Official%20Audio";
    }
    return keyword;
  }

  private BufferedReader search(String keyword) throws IOException {

    URL url = new URL(
        String.format(this.url, this.apiKey, keyword, this.type, this.part, this.order,
            this.relevanceLanguage, this.videoEmbeddable, this.maxResults, this.topicId));

    BufferedReader bufferedReader;

    bufferedReader = new BufferedReader(
        new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

    return bufferedReader;
  }

  private void jsonParsingToVideoList(List<Video> videoList, BufferedReader bufferedReader)
      throws ParseException, IOException {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(bufferedReader);
    JSONArray items = (JSONArray) jsonObject.get("items");

    for (int i = 0; i < items.size(); i++) {
      JSONObject item = (JSONObject) items.get(i);
      JSONObject id = (JSONObject) item.get("id");
      JSONObject snippet = (JSONObject) item.get("snippet");
      JSONObject thumbnails = (JSONObject) snippet.get("thumbnails");
      JSONObject thumbnail_default = (JSONObject) thumbnails.get("medium");

      Video video = Video.builder()
          .id((long) i)
          .videoId((String) id.get("videoId"))
          .videoName((String) snippet.get("title"))
          .videoThumbnail((String) thumbnail_default.get("url"))
          .build();

      videoList.add(video);
    }
  }
}