package com.zerobase.plistbackend.module.message.repository;

import com.zerobase.plistbackend.module.message.entity.Message;
import com.zerobase.plistbackend.module.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findAllByUser(User user);

  @Modifying
  @Query("UPDATE Message m SET m.read = true WHERE m.user = :user AND m.read = false")
  void updateAllMessageByUser(@Param("user") User user);
}
