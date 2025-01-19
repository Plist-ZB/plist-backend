package com.zerobase.plistbackend.module.user.repository;

import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByUserEmail(String email);

  @Query("SELECT new com.zerobase.plistbackend.module.user.dto.response.ProfileResponse(u.userEmail, u.userName, u.userImage) " +
      "FROM User u WHERE u.userEmail = :email")
  ProfileResponse findProfileByEmail(@Param("email") String email);
}
