package com.zerobase.plistbackend.module.channel.repository;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

  List<Channel> findAllByChannelStatus(boolean b);

  List<Channel> findByChannelStatusAndChannelNameLike(boolean b, String channelName);

  List<Channel> findByChannelStatusAndChannelCategory(boolean b, String channelCategory);
}
