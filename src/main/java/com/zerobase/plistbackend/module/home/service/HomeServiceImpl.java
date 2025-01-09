package com.zerobase.plistbackend.module.home.service;

import com.zerobase.plistbackend.common.app.config.YouTubeApi;
import com.zerobase.plistbackend.module.home.model.Video;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

  private final YouTubeApi youTubeApi;

  @Override
  @Transactional
  public List<Video> searchVideo(String keyword) throws IOException, ParseException {

    return youTubeApi.createVideoList(keyword);
  }
}
