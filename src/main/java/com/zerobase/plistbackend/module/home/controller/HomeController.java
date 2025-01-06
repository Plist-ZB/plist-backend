package com.zerobase.plistbackend.module.home.controller;

import com.zerobase.plistbackend.module.home.dto.response.VideoResponse;
import com.zerobase.plistbackend.module.home.service.HomeService;
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
public class HomeController {

  private final HomeService homeService;

  @GetMapping("/search-video")
  public ResponseEntity<List<VideoResponse>> searchVideo(@RequestParam("keyword") String keyword) {

    List<VideoResponse> videoResponseList = homeService.searchVideo(keyword);

    return ResponseEntity.ok(videoResponseList);
  }

}
