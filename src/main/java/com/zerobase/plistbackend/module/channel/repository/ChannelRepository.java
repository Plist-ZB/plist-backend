package com.zerobase.plistbackend.module.channel.repository;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

  List<Channel> findAllByChannelStatus(ChannelStatus channelStatus);

  List<Channel> findByChannelStatusAndChannelNameContaining(ChannelStatus channelStatus, String channelName);

  List<Channel> findByChannelStatusAndChannelCategory(ChannelStatus channelStatus, String channelCategory);
}
