package com.zerobase.plistbackend.module.websocket.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.home.model.Video;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class VideoSyncManagerTest {

  @Test
  void testConcurrentSyncVideo() throws InterruptedException {
    VideoSyncManager manager = new VideoSyncManager();

    int threadPoolSize = 32;
    ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);


    int threadCount = 100;
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      final Long channelId = (long) (i % 20);
      executor.submit(() -> {
        try {
          System.out.println("channelId = " + channelId);
          manager.updateCurrentTime(channelId, System.currentTimeMillis());
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
    executor.shutdown();

    assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    assertNotNull(manager.getCurrentTime(0L)); // 문제가 발생할 경우 null 반환
  }

  @Test
  void convertToJsonTest () throws JsonProcessingException {
    HashMap<String, List<Video>> map = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();
    map.put("PLAYLIST", List.of(new Video(1L, "asdasd", "asdasd.img", "asdasd"),
        new Video(2L, "asdasd", "asdasd.img", "asdasd")
        ,new Video(3L, "asdasd", "asdasd.img", "asdasd")));

    String json = mapper.writeValueAsString(map);
    System.out.println("json = " + json);
  }
}