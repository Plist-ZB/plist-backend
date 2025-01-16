package com.zerobase.plistbackend.module.websocket.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class WebSocketConnetionTest {

  @LocalServerPort
  private int port;

  @Test
  @DisplayName("WebSocket Connect 연결 성공")
  void success_connectWS() {
    // given
    WebSocketStompClient stompClient = new WebSocketStompClient(
        new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient())))
    );
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    String url = String.format("ws://localhost:%d/ws-connect", port);
    // when
    CompletableFuture<StompSession> stomp = stompClient.connectAsync(url,
        new StompSessionHandlerAdapter() {
          @Override
          public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            assertThat(session).isNotNull();
            assertThat(session.isConnected()).isEqualTo(true);
            System.out.println("Connected Result : " + session.isConnected());
            session.disconnect();
          }
        });
    stomp.join();

    // then
    assertThat(stomp.isDone()).isTrue();
  }

  @Test
  @DisplayName("WebSocket Connect 연결 테스트")
  void testConnectWebSocket() {
    // given
    WebSocketStompClient stompClient = new WebSocketStompClient(
        new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient())))
    );
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    String url = "ws://localhost:9999/ws-connect";
    // when
    CompletableFuture<StompSession> stomp = stompClient.connectAsync(url,
        new StompSessionHandlerAdapter() {
          @Override
          public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            Assertions.fail("웹소켓 연결 실패");
          }

          @Override
          public void handleTransportError(StompSession session, Throwable exception) {
            assertThat(session).isNotNull();
            session.disconnect();
            System.out.println("exception.getMessage() = " + exception.getMessage());
          }
        });
    //then
    Assertions.assertThatThrownBy(stomp::join)
        .isInstanceOf(CompletionException.class)
        .hasMessageContaining(
            " I/O error on GET request for \"http://localhost:9999/ws-connect/info\": Connection refused");
  }
}
