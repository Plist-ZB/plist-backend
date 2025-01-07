package com.zerobase.plistbackend.module.refresh.repository;

import com.zerobase.plistbackend.module.refresh.entity.Refresh;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

  Optional<Refresh> findByUser_UserEmail(String email);

  Optional<Refresh> findByRefreshToken(String token);

  @Transactional
  void deleteByRefreshToken(String refreshToken);
}
