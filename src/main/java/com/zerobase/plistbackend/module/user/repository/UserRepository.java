package com.zerobase.plistbackend.module.user.repository;

import com.zerobase.plistbackend.module.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.participant WHERE u.userEmail = :email")
  User findByUserEmail(@Param("email") String email);

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.participant WHERE u.userId = :userId")
  Optional<User> findByUserId(@Param("userId") Long userId);
}
