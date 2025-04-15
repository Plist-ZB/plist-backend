package com.zerobase.plistbackend.module.subscribe.service;

import com.zerobase.plistbackend.module.subscribe.domain.SubscribeEvent;
import com.zerobase.plistbackend.module.subscribe.dto.response.FollowerInfoResponse;
import com.zerobase.plistbackend.module.subscribe.dto.response.SubscribeResponse;
import com.zerobase.plistbackend.module.subscribe.entity.Subscribe;
import com.zerobase.plistbackend.module.subscribe.exception.SubscribeException;
import com.zerobase.plistbackend.module.subscribe.repository.SubscribeRepository;
import com.zerobase.plistbackend.module.subscribe.type.SubscribeErrorStatus;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.UserErrorStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

  private final UserRepository userRepository;
  private final SubscribeRepository subscribeRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  @Transactional(readOnly = true)
  public SubscribeResponse findFollowers(CustomOAuth2User customOAuth2User) {

    User followee = userRepository.findByUserEmail(customOAuth2User.findEmail());

    List<FollowerInfoResponse> followers = subscribeRepository.findByFollowee(followee);

    return SubscribeResponse.of(followers);
  }

  @Override
  @Transactional
  public void subscribe(CustomOAuth2User customOAuth2User, Long followerId) {

    User followee = userRepository.findByUserEmail(customOAuth2User.findEmail());

    User follower = userRepository.findByUserId(followerId).orElseThrow(() -> new UserException(
        UserErrorStatus.USER_NOT_FOUND));

    if (subscribeRepository.existsByFolloweeAndFollower(followee, follower)) {
      throw new SubscribeException(SubscribeErrorStatus.SUBSCRIBE_ALREADY_EXIST);
    }

    Subscribe subscribe = Subscribe.from(followee, follower);

    applicationEventPublisher.publishEvent(
        new SubscribeEvent(follower, followee.getUserId(), followee.getUserName()));
    subscribeRepository.save(subscribe);
  }

  @Override
  @Transactional
  public void unsubscribe(CustomOAuth2User customOAuth2User, Long followerId) {

    User followee = userRepository.findByUserEmail(customOAuth2User.findEmail());

    User follower = userRepository.findByUserId(followerId).orElseThrow(() -> new UserException(
        UserErrorStatus.USER_NOT_FOUND));

    Subscribe subscribe = subscribeRepository.findByFolloweeAndFollower(followee, follower)
        .orElseThrow(() -> new SubscribeException(SubscribeErrorStatus.SUBSCRIBE_NOT_FOUND));

    subscribeRepository.delete(subscribe);
  }
}