package com.zerobase.plistbackend.module.fcm.repository;

import com.zerobase.plistbackend.module.fcm.entity.FCMToken;
import com.zerobase.plistbackend.module.user.entity.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

  Optional<FCMToken> findByFcmTokenValue(String token);

  FCMToken findByUser(User followee);

  @Modifying
  @Transactional
  @Query("DELETE FROM FCMToken t WHERE t.fcmTokenCreatedAt < :threshold")
  int deleteFCMTokenOlderThan(LocalDateTime threshold);
}