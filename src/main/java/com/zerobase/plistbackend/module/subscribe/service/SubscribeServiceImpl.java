package com.zerobase.plistbackend.module.subscribe.service;

import com.zerobase.plistbackend.module.subscribe.dto.response.SubscribeResponse;
import com.zerobase.plistbackend.module.subscribe.entity.Subscribe;
import com.zerobase.plistbackend.module.subscribe.exception.SubscribeException;
import com.zerobase.plistbackend.module.subscribe.repository.SubscribeRepository;
import com.zerobase.plistbackend.module.subscribe.type.SubscribeErrorStatus;
import com.zerobase.plistbackend.module.subscribe.dto.response.FolloweeInfoResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.UserErrorStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

  private final UserRepository userRepository;
  private final SubscribeRepository subscribeRepository;

  @Override
  @Transactional(readOnly = true)
  public SubscribeResponse findFollowees(CustomOAuth2User customOAuth2User) {

    User follower = userRepository.findByUserEmail(customOAuth2User.findEmail());

    List<FolloweeInfoResponse> followees = subscribeRepository.findByFollower(follower);

    return SubscribeResponse.of(followees);
  }

  @Override
  @Transactional
  public void subcribe(CustomOAuth2User customOAuth2User, Long followeeId) {

    User follower = userRepository.findByUserEmail(customOAuth2User.findEmail());

    User followee = userRepository.findByUserId(followeeId).orElseThrow(() -> new UserException(
        UserErrorStatus.USER_NOT_FOUND));

    if (subscribeRepository.existsByFollowerAndFollowee(follower, followee)) {
      throw new SubscribeException(SubscribeErrorStatus.SUBSCRIBE_ALREADY_EXIST);
    }

    Subscribe subscribe = Subscribe.from(follower, followee);

    subscribeRepository.save(subscribe);
  }

  @Override
  @Transactional
  public void unsubscribe(CustomOAuth2User customOAuth2User, Long followeeId) {

    User follower = userRepository.findByUserEmail(customOAuth2User.findEmail());

    User followee = userRepository.findByUserId(followeeId).orElseThrow(() -> new UserException(
        UserErrorStatus.USER_NOT_FOUND));

    Subscribe subscribe = subscribeRepository.findByFollowerAndFollowee(follower, followee)
        .orElseThrow(() -> new SubscribeException(SubscribeErrorStatus.SUBSCRIBE_NOT_FOUND));

    subscribeRepository.delete(subscribe);
  }
}