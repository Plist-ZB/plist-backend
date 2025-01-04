package com.zerobase.plistbackend.module.home.controller;

import com.zerobase.plistbackend.module.home.dto.response.VideoResponse;
import com.zerobase.plistbackend.module.home.service.HomeService;
import com.zerobase.plistbackend.module.userplaylist.domain.Video;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Application의 Home API", description = "Home에서 검색기능을 담당하는 API Controller")
public class HomeController {

  private final HomeService homeService;

  @GetMapping("/service/search")
  public ResponseEntity<List<VideoResponse>> searchVideo(@RequestParam("searchValue") String searchValue) {

    List<VideoResponse> videoResponseList = homeService.searchVideo(searchValue);

    return ResponseEntity.ok(videoResponseList);
  }

  @Operation(
      summary = "Video를 단건으로 조회하는 API 입니다.",
      description = "전체 검색으로 VideoList를 끌고와서  UserPlaylist에 영상 추가시 사용될 API 입니다."
  )
  @GetMapping("/service/search/{videoId}")
  public ResponseEntity<Video> getVideo(@PathVariable String videoId) throws IOException {
    return ResponseEntity.ok(homeService.getVideo(videoId));
  }
}
