package com.zerobase.plistbackend.module.home.controller;

import com.zerobase.plistbackend.module.home.dto.response.VideoResponse;
import com.zerobase.plistbackend.module.home.service.HomeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

  @GetMapping("/search-video")
  public ResponseEntity<List<VideoResponse>> searchVideo(@RequestParam("keyword") String keyword) {

    List<VideoResponse> videoResponseList = homeService.searchVideo(keyword);

    return ResponseEntity.ok(videoResponseList);
  }
}
