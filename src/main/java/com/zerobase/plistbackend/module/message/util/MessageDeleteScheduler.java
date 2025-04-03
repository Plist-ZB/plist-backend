package com.zerobase.plistbackend.module.message.util;

import com.zerobase.plistbackend.module.message.repository.MessageRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageDeleteScheduler {

  private final MessageRepository messageRepository;

  @Scheduled(cron = "0 0 0 * * ?")
  public void deleteAfter7DaysMessages() {
    LocalDateTime threshold = LocalDate.now().minusDays(7).atStartOfDay();
    int deleteCount = messageRepository.deleteMessagesOlderThan(threshold);
    log.info("메시지 삭제 스케쥴러 동작시간 : {}, 총 {}개의 메시지를 삭제하였습니다.", threshold, deleteCount);
  }
}
