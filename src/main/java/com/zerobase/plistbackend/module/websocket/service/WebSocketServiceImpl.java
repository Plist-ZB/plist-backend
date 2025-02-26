package com.zerobase.plistbackend.module.websocket.service;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.exception.ChannelException;
import com.zerobase.plistbackend.module.channel.repository.ChannelRepository;
import com.zerobase.plistbackend.module.channel.type.ChannelErrorStatus;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.OAuth2UserException;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.OAuth2UserErrorStatus;
import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.websocket.dto.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebSocketServiceImpl implements WebSocketService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ChatMessageResponse sendMessage(ChatMessageRequest request) {
    User findedUser = userRepository.findByUserEmail(request.getEmail());
    if (findedUser == null) {
      throw new OAuth2UserException(OAuth2UserErrorStatus.NOT_FOUND);
    }
    return ChatMessageResponse.from(request, findedUser);
  }

  @Override
  public boolean isHost(Long channelId, String email) {
    Channel findedChannel = channelRepository.findByChannelIdAndChannelStatus(channelId,
            ChannelStatus.CHANNEL_STATUS_ACTIVE)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    User findedUser = userRepository.findByUserEmail(email);

    return findedChannel.getChannelHost().equals(findedUser);
  }

}
