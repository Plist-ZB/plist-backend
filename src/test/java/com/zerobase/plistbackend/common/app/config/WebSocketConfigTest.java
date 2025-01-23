// package com.zerobase.plistbackend.common.app.config;

// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.List;
// import java.util.concurrent.TimeUnit;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.messaging.converter.MappingJackson2MessageConverter;
// import org.springframework.messaging.simp.stomp.StompSession;
// import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
// import org.springframework.web.socket.client.standard.StandardWebSocketClient;
// import org.springframework.web.socket.messaging.WebSocketStompClient;
// import org.springframework.web.socket.sockjs.client.SockJsClient;
// import org.springframework.web.socket.sockjs.client.Transport;
// import org.springframework.web.socket.sockjs.client.WebSocketTransport;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// class WebSocketConfigTest {

//   private static final String WEBSOCKET_ENDPOINT = "/ws-connect";

//   @Value("${local.server.port}")
//   private int port;


//   @Test
//   void testWebSocketConnection() throws Exception {
//     //given
//     List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
//     SockJsClient sockJsClient = new SockJsClient(transports);

//     WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
//     stompClient.setMessageConverter(new MappingJackson2MessageConverter());

//     String url = String.format("http://localhost:%d%s", port, WEBSOCKET_ENDPOINT);

//     //when
//     StompSession stompSession = stompClient
//         .connectAsync(url, new StompSessionHandlerAdapter() {})
//         .get(1, TimeUnit.SECONDS);

//     //then
//     assertNotNull(stompSession, "WebSocket connection should be established.");
//     assertTrue(stompSession.isConnected());
//   }

// }
