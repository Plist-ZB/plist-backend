package com.zerobase.plistbackend.module.channel.service;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.VideoResponse;
import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.repository.ChannelRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

  private static final String GOOGLE_API_URL = "https://www.googleapis.com/youtube/v3/search?key=%s&q=%s&type=%s&part=%s&maxResults=%d&order=%s&relevanceLanguage=%s&videoEmbeddable=%s";
  private static final String TYPE = "video"; // 검색할 타입
  private static final String PART = "snippet"; // 응답에 포함될 내용
  private static final int MAX_RESULTS = 50; // 페이지 당 보여주는 응답 갯수 범위 0~50
  private static final String ORDER = "viewCount"; // 조회수가 많은 순서대로 정렬 (others : date 업로드 날짜순, rating 평점순, relevance 가장 검색어에 가까운 순(default), title (제목 알파벳 순))
  private static final String RELEVANCE_LANGUAGE = "ko"; // 가장 관련있는 언어로 검색, 우선 한국어가 우선 이후 관련된 언어도 같이 검색
  private static final String VIDEO_EMBEDDABLE = "true"; // 웹 페이지에서 재생가능한 동영상만 검색
  private static String YOUTUBE_DATA_API_KEY; // YOUTUBE DATA API KEY
  private final ChannelRepository channelRepository;

  @Value("${youtube_data_api_key}")
  public void setYoutubeDataApiKey(String youtubeDataApiKey) {
    ChannelServiceImpl.YOUTUBE_DATA_API_KEY = youtubeDataApiKey;
  }

  @Override
  public ChannelResponse addChannel(ChannelRequest channelRequest) {

    Channel channel = Channel.createChannel(channelRequest);

    channelRepository.save(channel);

    return ChannelResponse.createChannelResponse(channel);
  }

  @Override
  public List<ChannelResponse> findChannelList() {
    List<Channel> channelList = channelRepository.findAll();

    List<Channel> streamingChannelList = channelList.stream()
        .filter(channel -> channel.getChannelFinishedAt() != null).toList();

    return streamingChannelList.stream().map(
        ChannelResponse::createChannelResponse).toList();
  }

  @Override
  public List<ChannelResponse> findChannelFromChannelName(String channelName) {
    List<Channel> channelList = channelRepository.findByChannelNameLike(
        channelName).orElseThrow(() -> new RuntimeException("검색어에 해당하는 채널이 없습니다."));// TODO : 예외처리

    List<Channel> streamingChannelList = channelList.stream()
        .filter(channel -> channel.getChannelFinishedAt() != null).toList();

    return streamingChannelList.stream().map(ChannelResponse::createChannelResponse).toList();
  }

  @Override
  public List<ChannelResponse> findChannelFromChannelCategory(String channelCategory) {
    List<Channel> channelList = channelRepository.findByChannelCategory(channelCategory)
        .orElseThrow(() -> new RuntimeException("검색하는 카테고리에 해당하는 채널이 없습니다."));// TODO : 예외처리

    List<Channel> streamingChannelList = channelList.stream()
        .filter(channel -> channel.getChannelFinishedAt() != null).toList();

    return streamingChannelList.stream().map(ChannelResponse::createChannelResponse).toList();
  }

  @Override
  public List<VideoResponse> searchVideo(String searchValue) {

    List<VideoResponse> videoResponseList = new ArrayList<>();
    String result = "";

    try {
      URL url = new URL(
          String.format(GOOGLE_API_URL, YOUTUBE_DATA_API_KEY, searchValue, TYPE, PART, MAX_RESULTS,
              ORDER, RELEVANCE_LANGUAGE, VIDEO_EMBEDDABLE));

      BufferedReader br;

      br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

      StringBuilder sb = new StringBuilder();
      String inputLine;
      while ((inputLine = br.readLine()) != null) {
        sb.append(inputLine);
      }
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
}
