package com.zerobase.plistbackend.common.server;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerCheckController {

  @Value("${server.env}")
  private String env;

  @Value("${server.serverAddress}")
  private String serverAddress;

  @Value("${server.port}")
  private String port;

  @GetMapping("/hc")
  public ResponseEntity<?> deleteSomething() {
    HashMap<String, String> checkCurrentStatusMap = new HashMap<>();
    checkCurrentStatusMap.put("ServerEnvironment", env);
    checkCurrentStatusMap.put("ServerAddress", serverAddress);
    checkCurrentStatusMap.put("ServerPort", port);
    return ResponseEntity.ok(checkCurrentStatusMap);
  }

}
