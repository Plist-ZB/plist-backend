package com.zerobase.plistbackend.module.channel.repository;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.user.entity.User;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

  @Query("SELECT c FROM Channel c " +
      "LEFT JOIN c.channelParticipants p " +
      "WHERE c.channelStatus = :channelStatus " +
      "AND (c.channelName LIKE %:channelName% OR c.category.categoryName LIKE %:categoryName% OR c.channelHost.userName LIKE %:channelHost%) "
      +
      "GROUP BY c.channelId " +
      "ORDER BY SIZE(p) DESC")
  List<Channel> search(
      @Param("channelStatus") ChannelStatus channelStatus,
      @Param("channelName") String channelName,
      @Param("categoryName") String categoryName,
      @Param("channelHost") String channelHost);

  @Query("SELECT c FROM Channel c " +
      "JOIN FETCH c.channelPlaylist p " +
      "JOIN FETCH c.channelParticipants cp " +
      "WHERE c.channelId = :channelId AND c.channelStatus = :channelStatus")
  Optional<Channel> findByChannelIdAndChannelStatus(@Param("channelId") Long channelId,
      @Param("channelStatus") ChannelStatus channelStatus);

  @Query("SELECT c FROM Channel c " +
      "JOIN FETCH c.channelPlaylist " +
      "JOIN FETCH c.channelHost " +
      "JOIN FETCH c.category " +
      "WHERE c.channelId = :channelId AND c.channelHost = :user")
  Optional<Channel> findByChannelIdAndChannelHost(@Param("channelId") Long channelId,
      @Param("user") User user);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select c from Channel c join fetch c.channelPlaylist where c.channelId = :channelId")
  Optional<Channel> findByIdWithLock(@Param("channelId") Long channelId);

  @Query("select c from Channel c join fetch c.channelPlaylist where c.channelId = :channelId")
  Optional<Channel> findByIdFetchJoinPlaylist(@Param("channelId") Long channelId);

  @Query("SELECT c FROM Channel c " +
      "JOIN FETCH c.channelHost " +
      "WHERE c.channelHost.userId = :hostId " +
      "AND c.channelStatus = :channelStatus " +
      "AND c.channelCreatedAt >= :startDate " +
      "AND c.channelCreatedAt < :endDate")
  List<Channel> findByChannelHostId(
      @Param("hostId") Long hostId,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      @Param("channelStatus") ChannelStatus channelStatus
  );

  boolean existsByChannelHost_UserIdAndChannelStatus(Long userId, ChannelStatus status);
}