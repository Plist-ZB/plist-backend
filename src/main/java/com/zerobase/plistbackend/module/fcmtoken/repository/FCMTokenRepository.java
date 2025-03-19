package com.zerobase.plistbackend.module.fcmtoken.repository;

import com.zerobase.plistbackend.module.fcmtoken.entity.FCMToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

  Optional<FCMToken> findByFcmTokenValue(String token);
}