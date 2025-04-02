package com.zerobase.plistbackend.module.subscribe.repository;

import com.zerobase.plistbackend.module.subscribe.entity.Subscribe;
import com.zerobase.plistbackend.module.subscribe.dto.response.FollowerInfoResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

  @Query(
      "SELECT new com.zerobase.plistbackend.module.subscribe.dto.response.FollowerInfoResponse(s.follower.userId, s.follower.userName, s.follower.userImage) "
          + "FROM Subscribe s "
          + "WHERE s.followee = :followee")
  List<FollowerInfoResponse> findByFollowee(User followee);

  Optional<Subscribe> findByFolloweeAndFollower(User followee, User follower);

  boolean existsByFolloweeAndFollower(User followee, User follower);

  @Query(
      "SELECT s.follower.userId "
          + "FROM Subscribe s "
          + "WHERE s.followee.userId = :followeeId")
  List<Long> findFollowersIdByFolloweeId(Long followeeId);
}