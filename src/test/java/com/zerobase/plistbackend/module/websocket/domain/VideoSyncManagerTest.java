package com.zerobase.plistbackend.module.websocket.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}