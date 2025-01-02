package com.zerobase.plistbackend.module.home.controller;

import com.zerobase.plistbackend.module.home.dto.response.VideoResponse;
import com.zerobase.plistbackend.module.home.service.HomeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

  private final HomeService homeService;

  @GetMapping("/service/search")
  public ResponseEntity<List<VideoResponse>> searchVideo(@RequestParam("searchValue") String searchValue) {

    List<VideoResponse> videoResponseList = homeService.searchVideo(searchValue);

    return ResponseEntity.ok(videoResponseList);
  }

}
