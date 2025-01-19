package com.zerobase.plistbackend.module.channel.repository;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

  @Query("SELECT c FROM Channel c WHERE c.channelStatus = :channelStatus ORDER BY c.channelId DESC")
  List<Channel> findAllByChannelStatusSortedChannelIdDesc(
      @Param("channelStatus") ChannelStatus channelStatus);

  @Query("SELECT c FROM Channel c " +
      "LEFT JOIN c.channelParticipants p " +
      "LEFT JOIN User u ON u.userId = c.channelHostId " +
      "WHERE c.channelStatus = :channelStatus " +
      "AND (c.channelName LIKE %:channelName% OR c.category.categoryName LIKE %:categoryName% OR u.userName LIKE %:channelHostUserName%) " +
      "GROUP BY c.channelId " +
      "ORDER BY SIZE(p) DESC")
  List<Channel> search(
      @Param("channelStatus") ChannelStatus channelStatus,
      @Param("channelName") String channelName,
      @Param("categoryName") String categoryName,
      @Param("channelHostUserName") String channelHostUserName);

  @Query("SELECT c FROM Channel c LEFT JOIN c.channelParticipants p " +
      "WHERE c.channelStatus = :channelStatus " +
      "AND c.category.categoryId = :categoryId " +
      "GROUP BY c.channelId " +
      "ORDER BY COUNT(p) DESC")
  List<Channel> findByChannelStatusAndCategorySortedParticipantCountDesc(
      @Param("channelStatus") ChannelStatus channelStatus,
      @Param("categoryId") Long categoryId);

  @Query("SELECT c FROM Channel c LEFT JOIN c.channelParticipants p " +
      "WHERE c.channelStatus = :channelStatus " +
      "GROUP BY c.channelId " +
      "ORDER BY COUNT(p) DESC")
  List<Channel> findAllByChannelStatusSortedByParticipantCountDesc(
      @Param("channelStatus") ChannelStatus channelStatus
  );

  List<Channel> findByChannelHostIdAndChannelStatus(Long userId, ChannelStatus channelStatus);

  @Query("SELECT c FROM Channel c " +
      "LEFT JOIN FETCH c.channelPlaylist p " +
      "LEFT JOIN FETCH c.channelParticipants cp " +
      "LEFT JOIN FETCH cp.user u " +
      "WHERE c.channelId = :channelId AND c.channelStatus = :channelStatus")
  Optional<Channel> findByChannelIdAndChannelStatus(@Param("channelId") Long channelId,
      @Param("channelStatus") ChannelStatus channelStatus);

  Optional<Channel> findByChannelIdAndChannelHostId(Long channelId, Long userId);
}