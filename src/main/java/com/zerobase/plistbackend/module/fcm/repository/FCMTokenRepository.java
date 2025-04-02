package com.zerobase.plistbackend.module.fcm.repository;

import com.zerobase.plistbackend.module.fcm.entity.FCMToken;
import com.zerobase.plistbackend.module.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

  Optional<FCMToken> findByFcmTokenValue(String token);

  FCMToken findByUser(User followee);
}