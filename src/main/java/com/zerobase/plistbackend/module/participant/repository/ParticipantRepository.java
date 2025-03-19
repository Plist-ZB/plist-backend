package com.zerobase.plistbackend.module.participant.repository;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    boolean existsByUser(User user);

    List<Participant> findByChannel(Channel channel);

    Participant findByUser(User user);

    @Query(
            "select p from Participant p where p.user.userId = :userId and p.participantCreatedAt >= :startDate and p.participantCreatedAt < :endDate"
    )
    List<Participant> findByUserIdAndDate(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
