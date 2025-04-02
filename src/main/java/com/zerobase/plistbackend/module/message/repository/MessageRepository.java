package com.zerobase.plistbackend.module.message.repository;

import com.zerobase.plistbackend.module.message.entity.Message;
import com.zerobase.plistbackend.module.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findAllByUser(User user);

  @Modifying
  @Query("UPDATE Message m SET m.readCheck = true WHERE m.user = :user AND m.readCheck = false")
  void updateAllMessageByUser(@Param("user") User user);

  boolean existsByUserAndReadCheck(User user, boolean readCheck);

  @Modifying
  @Transactional
  @Query("DELETE FROM Message m WHERE m.messageCreatedAt < :threshold")
  int deleteMessagesOlderThan(@Param("threshold") LocalDateTime threshold);
}
