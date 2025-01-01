package com.zerobase.plistbackend.module.channel.repository;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

  Optional<List<Channel>> findByChannelNameLike(String channelName);

  Optional<List<Channel>> findByChannelCategory(String channelCategory);
}
