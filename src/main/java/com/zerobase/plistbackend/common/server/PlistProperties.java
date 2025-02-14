package com.zerobase.plistbackend.common.server;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PlistProperties {
  @Value("${server.port}")
  private String port;

  @Value("${serverName}")
  private String serverName;

  @Value("${server.serverAddress}")
  private String serverAddress;

  @Value("${google}")
  private String googleLoginUrl;

  @Value("${naver}")
  private String naverLoginUrl;

  @Value("${kakao}")
  private String kakaoLoginUrl;

  @Value("${db_class_name}")
  private String dbClassName;

}
