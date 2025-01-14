package com.zerobase.plistbackend.common.server;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "ServerCheckController", description = "현재 서버 상태 확인용 API")
public class ServerCheckController {

  private final PlistProperties plistProperties;


  @GetMapping("/sc")
  public ResponseEntity<?> checkCurrentServer() {
    return ResponseEntity.ok(plistProperties);
  }

  @GetMapping("/env")
  public ResponseEntity<?> checkCurrentEnv() {
    return ResponseEntity.ok(plistProperties.getServerName());
  }
}
