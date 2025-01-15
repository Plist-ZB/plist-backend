package com.zerobase.plistbackend.module.websocket.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class VideoSyncManagerTest {

  @Test
  void testConcurrentSyncVideo() throws InterruptedException {
    VideoSyncManager manager = new VideoSyncManager();
    ExecutorService executor = Executors.newFixedThreadPool(10);

    for (int i = 0; i < 100; i++) {
      final Long channelId = (long) (i % 10);
      executor.submit(() -> manager.updateCurrentTime(channelId, System.currentTimeMillis()));
    }

    executor.shutdown();
    assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    assertNotNull(manager.getCurrentTime(0L));
  }
}