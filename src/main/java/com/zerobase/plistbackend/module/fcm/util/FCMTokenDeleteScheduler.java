package com.zerobase.plistbackend.module.fcm.util;

import com.zerobase.plistbackend.module.fcm.repository.FCMTokenRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMTokenDeleteScheduler {

  private final FCMTokenRepository fcmTokenRepository;

  @Scheduled(cron = "0 0 0 1 * ?")
  public void deleteAfter7DaysMessages() {
    LocalDateTime threshold = LocalDate.now().minusYears(1).atStartOfDay();
    int deleteCount = fcmTokenRepository.deleteFCMTokenOlderThan(threshold);
    log.info("FCM토큰 삭제 스케쥴러 동작시간 : {}, 총 {}개의 FCM토큰을 삭제하였습니다.", threshold, deleteCount);
  }
}