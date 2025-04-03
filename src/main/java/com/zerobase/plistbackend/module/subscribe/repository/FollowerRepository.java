package com.zerobase.plistbackend.module.subscribe.repository;

import com.zerobase.plistbackend.module.subscribe.entity.Subscribe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowerRepository extends JpaRepository<Subscribe, Long> {

  @Query(
      "SELECT f.fcmTokenValue FROM Subscribe s "
          + "LEFT JOIN FCMToken f ON s.follower.userId = f.user.userId "
          + "WHERE s.followee.userId = :followeeId")
  List<String> findFollowersFCMtokenByFolloweeId(Long followeeId);
}
