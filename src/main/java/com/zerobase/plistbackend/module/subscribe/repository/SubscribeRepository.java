package com.zerobase.plistbackend.module.subscribe.repository;

import com.zerobase.plistbackend.module.subscribe.entity.Subscribe;
import com.zerobase.plistbackend.module.user.dto.response.UserInfoResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

  @Query(
      "SELECT new com.zerobase.plistbackend.module.user.dto.response.UserInfoResponse(s.followee.userId, s.followee.userName) "
          + "FROM Subscribe s "
          + "WHERE s.follower = :follower")
  List<UserInfoResponse> findByFollower(User follower);

  Optional<Subscribe> findByFollowerAndFollowee(User follower, User followee);
}