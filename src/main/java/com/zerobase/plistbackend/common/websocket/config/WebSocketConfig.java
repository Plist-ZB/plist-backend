package com.zerobase.plistbackend.common.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private static final String[] SUBSCRIBE_PREFIX = {"/sub"};
  private static final String[] PUBLISH_PREFIX = {"/pub"};
  private static final String[] STOMP_CONNECT_URLS = {"/ws-connect"};
  private static final String[] ALLOWED_ORIGINS_URLS = {"http://localhost:3000"};


  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker(SUBSCRIBE_PREFIX);
    registry.setApplicationDestinationPrefixes(PUBLISH_PREFIX);
  }

  /**
   * setAllowedOrigins: React Port 3000
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(STOMP_CONNECT_URLS)
        .setAllowedOrigins(ALLOWED_ORIGINS_URLS)
        .withSockJS();
  }
}