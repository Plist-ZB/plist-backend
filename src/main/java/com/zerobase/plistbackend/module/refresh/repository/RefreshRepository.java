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

  public void saveToken(Long userId, String token) {
    redisTemplate.opsForValue().set(userId.toString(), token, EXPIRATION_TIME, TimeUnit.SECONDS);
  }

  public Boolean hasToken(Long userId) {
    return redisTemplate.hasKey(userId.toString());
  }

  public String findTokenByUserId(Long userId) {
    return redisTemplate.opsForValue().get(userId.toString()).toString();
  }

  public void deleteByToken(Long userId) {
    redisTemplate.delete(userId.toString());
  }

}
