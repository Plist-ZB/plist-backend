package com.zerobase.plistbackend.module.refresh.repository;

import com.zerobase.plistbackend.module.refresh.exception.RefreshException;
import com.zerobase.plistbackend.module.refresh.type.RefreshErrorStatus;
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
    Object token = redisTemplate.opsForValue().get(userId.toString());
    if (token == null) {
      throw new RefreshException(RefreshErrorStatus.REFRESH_NOT_FOUND);
    }
    return token.toString();
  }

  public void deleteByToken(Long userId) {
    redisTemplate.delete(userId.toString());
  }

}
