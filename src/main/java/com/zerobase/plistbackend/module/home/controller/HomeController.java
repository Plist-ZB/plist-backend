package com.zerobase.plistbackend.module.home.controller;

import com.zerobase.plistbackend.module.home.service.HomeService;
import com.zerobase.plistbackend.module.home.model.Video;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/api")
@RequiredArgsConstructor
@Tag(name = "Application의 Home API", description = "Home에서 검색기능을 담당하는 API Controller")
public class HomeController {

  private final HomeService homeService;

  @Operation(
      summary = "Youtube 영상 검색 기능",
      description = "검색어를 통해 Youtube Data Api 검색 결과를 가져옵니다."
          + "**주의** 해당 기능은 API KEY 한도가 하루 최대 100회입니다."
  )
  @GetMapping("/search-video")
  public ResponseEntity<List<Video>> searchVideo(@RequestParam("keyword") String keyword)
      throws IOException, ParseException {

    List<Video> videoList = homeService.searchVideo(keyword);

    return ResponseEntity.ok(videoList);
  }
}
