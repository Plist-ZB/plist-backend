package com.zerobase.plistbackend.module.message.repository;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageBatchRepository {

  private final JdbcTemplate jdbcTemplate;

  public void batchInsertMessages(List<Long> followersIds, String message, String link) {
    String sql = "INSERT INTO message (user_id, message_created_at, message_content, message_link, read_check) VALUES (?, now(), ?, ?, ?)";

    List<Object[]> batchArgs = new ArrayList<>();
    for (Long followerId : followersIds) {
      batchArgs.add(new Object[]{followerId, message, link, false});
    }

    jdbcTemplate.batchUpdate(sql, batchArgs);
  }

  public void batchDeleteMessages(List<Long> followersIds) {
    String sql = "DELETE FROM message WHERE user_id = ?";

    List<Object[]> batchArgs = new ArrayList<>();
    for (Long followerId : followersIds) {
      batchArgs.add(new Object[]{followerId});
    }

    jdbcTemplate.batchUpdate(sql, batchArgs);
  }
}
