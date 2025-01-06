package com.zerobase.plistbackend.module.home.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.common.app.config.YouTubeApiProperties;
import com.zerobase.plistbackend.common.app.exception.JsonParseException;
import com.zerobase.plistbackend.common.app.type.JsonErrorStatus;
import com.zerobase.plistbackend.module.userplaylist.domain.Video;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class HomeControllerTest {

  @Autowired
  private YouTubeApiProperties youTubeApiProperties;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("영상 단건 조회")
  void success_searchOne_Video() throws IOException {
    // given
    String videoId = "Ks-_Mh1QhMc"; // 실제 YouTube 비디오 ID (예시)

    // When
    Video result = youTubeApiProperties.createVideo(videoId, objectMapper);

    // Then
    assertNotNull(result);
    assertEquals("Ks-_Mh1QhMc", result.getVideoId());
    assertNotNull(result.getVideoName());
    assertNotNull(result.getVideoThumbnail());
  }

  @Test
  @DisplayName("영상 단건 조회 - 서버 장애 시")
  void error_searchOne_Video() {
    // given
    String videoId = "InvalidVideoId";

    // when
    MockedStatic<URL> mockedUrl = Mockito.mockStatic(URL.class);
    mockedUrl.when(() -> new URL(String.format(youTubeApiProperties.getSelectVideoUrl(),
            youTubeApiProperties.getApiKey(), videoId)))
        .thenThrow(new JsonParseException(JsonErrorStatus.SEVER_ERROR));

    // Then
    JsonParseException exception = assertThrows(JsonParseException.class, () -> {
      youTubeApiProperties.createVideo(videoId, objectMapper);
    });

    assertEquals("JSON 데이터 파싱 or URL 네트워크 에러 발생!!", exception.getMessage());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getErrorCode());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getErrorType());
    mockedUrl.close();
  }
}