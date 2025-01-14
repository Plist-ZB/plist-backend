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

  @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
  private String loginRedirectUri;

  @Value("${spring.jpa.hibernate.ddl-auto}")
  private String hibernateDdlAuto;

  @Value("${db_class_name}")
  private String dbClassName;
}
