package com.zerobase.plistbackend.module.refresh.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  private static final Long EXPIRATION_TIME = 478800000L;
  //private static final Long EXPIRATION_TIME = 10L;

  public void saveToken(Long userId, String token) {
    redisTemplate.opsForValue().set(token, userId.toString(), EXPIRATION_TIME, TimeUnit.SECONDS);
  }

  public Boolean hasToken(String token) {
    return redisTemplate.hasKey(token);
  }

  public void deleteByToken(String refreshToken) {
    redisTemplate.delete(refreshToken);
  }

}
