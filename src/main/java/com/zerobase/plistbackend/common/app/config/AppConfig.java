package com.zerobase.plistbackend.common.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.common.app.domain.YouTubeApiProperties;
import jakarta.persistence.EntityListeners;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@EntityListeners(AuditingEntityListener.class)
@EnableConfigurationProperties(YouTubeApiProperties.class)
public class AppConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}

