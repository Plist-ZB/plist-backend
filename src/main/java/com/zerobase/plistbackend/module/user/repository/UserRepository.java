package com.zerobase.plistbackend.module.user.repository;

import com.zerobase.plistbackend.module.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByUserEmail(String email);

  Optional<User> findByUserName(String sender);
}
