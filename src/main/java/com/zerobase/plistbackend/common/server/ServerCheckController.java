package com.zerobase.plistbackend.common.server;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServerCheckController {

  private final PlistProperties plistProperties;


  @GetMapping("/sc")
  public ResponseEntity<?> deleteSomething() {
    return ResponseEntity.ok(plistProperties);
  }
}
