package com.zerobase.plistbackend.module.channel.service;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ChannelResponse;
import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.repository.ChannelRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

  // 웹 페이지에서 재생가능한 동영상만 검색
  private final ChannelRepository channelRepository;


  @Override
  @Transactional
  public ChannelResponse addChannel(ChannelRequest channelRequest) {

    Channel channel = Channel.createChannel(channelRequest);

    channelRepository.save(channel);

    return ChannelResponse.createChannelResponse(channel);
  }

  @Override
  @Transactional
  public List<ChannelResponse> findChannelList() {
    List<Channel> channelList = channelRepository.findAllByChannelStatus(true);

    return channelList.stream().map(
        ChannelResponse::createChannelResponse).toList();
  }

  @Override
  @Transactional
  public List<ChannelResponse> findChannelFromChannelName(String channelName) {
    List<Channel> channelList = channelRepository.findByChannelStatusAndChannelNameLike(true,
        channelName);// TODO : 예외처리

    return channelList.stream().map(ChannelResponse::createChannelResponse).toList();
  }

  @Override
  @Transactional
  public List<ChannelResponse> findChannelFromChannelCategory(String channelCategory) {
    List<Channel> channelList = channelRepository.findByChannelStatusAndChannelCategory(true,
        channelCategory);// TODO : 예외처리

    return channelList.stream().map(ChannelResponse::createChannelResponse).toList();
  }
}
