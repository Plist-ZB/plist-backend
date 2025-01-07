package com.zerobase.plistbackend.module.participant.repository;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

  void deleteByUser(User user);

  void deleteByChannel(Channel channel);

  List<Participant> findByUser(User user);
}