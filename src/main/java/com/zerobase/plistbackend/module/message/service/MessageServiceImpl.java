package com.zerobase.plistbackend.module.message.service;

import com.zerobase.plistbackend.module.message.dto.response.MessageResponse;
import com.zerobase.plistbackend.module.message.dto.response.UnreadResponse;
import com.zerobase.plistbackend.module.message.entity.Message;
import com.zerobase.plistbackend.module.message.exception.MessageException;
import com.zerobase.plistbackend.module.message.repository.MessageRepository;
import com.zerobase.plistbackend.module.message.type.MessageErrorStatus;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

  private final UserRepository userRepository;
  private final MessageRepository messageRepository;

  @Override
  @Transactional(readOnly = true)
  public List<MessageResponse> findMessages(CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    List<Message> messageList = messageRepository.findAllByUser(user);

    return messageList.stream().map(MessageResponse::of).toList();
  }

  @Override
  @Transactional
  public void readMessage(CustomOAuth2User customOAuth2User, Long messageId) {

    Message message = messageRepository.findById(messageId).orElseThrow(() -> new MessageException(
        MessageErrorStatus.NOT_FOUND));

    if (!message.getUser().getUserId().equals(customOAuth2User.findId())) {
      throw new MessageException(MessageErrorStatus.NOT_MY_MESSAGE);
    }

    message.read();
  }

  @Override
  @Transactional
  public void readAllMessage(CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    messageRepository.updateAllMessageByUser(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UnreadResponse checkUnreadMessage(CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    boolean result = messageRepository.existsByUserAndReadCheck(user, false);

    return UnreadResponse.of(result);
  }
}
