package com.zerobase.plistbackend.module.chatting.service;

import com.zerobase.plistbackend.module.chatting.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.chatting.dto.response.ChatMessageResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.OAuth2UserException;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.OAuth2UserErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

  private final UserRepository userRepository;

  @Override
  public ChatMessageResponse sendMessage(ChatMessageRequest request) {
    User findUser = userRepository.findByUserName(request.getSender())
        .orElseThrow(() -> new OAuth2UserException(OAuth2UserErrorStatus.NOT_FOUND));
    return ChatMessageResponse.from(request, findUser.getUserImage());
  }

  @Override
  public boolean isHost(CustomOAuth2User user) {
    // TODO -> User의 Role을 해두고 Channel을 생성시 현재 UserRole을 HOST로, channel 폭파시에는 UserRole을 USER로
    return false;
  }
}
