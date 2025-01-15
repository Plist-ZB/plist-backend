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

  @Query("SELECT c FROM Channel c LEFT JOIN c.channelParticipants p " +
      "WHERE c.channelStatus = :channelStatus " +
      "AND (c.channelName LIKE %:channelName% OR c.category.categoryName LIKE %:categoryName% OR c.channelHost LIKE %:channelHost%) "
      +
      "GROUP BY c.channelId " +
      "ORDER BY COUNT(p) DESC")
  List<Channel> search(
      @Param("channelStatus") ChannelStatus channelStatus,
      @Param("channelName") String channelName,
      @Param("categoryName") String categoryName,
      @Param("channelHost") String channelHost);

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

  List<Channel> findByChannelHostAndChannelStatus(String userName, ChannelStatus channelStatus);

  Optional<Channel> findByChannelIdAndChannelStatus(Long channelId,
      ChannelStatus channelStatusActive);
}