package com.zerobase.plistbackend.module.user.service;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.repository.ChannelRepository;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.participant.repository.ParticipantRepository;
import com.zerobase.plistbackend.module.subscribe.repository.SubscribeRepository;
import com.zerobase.plistbackend.module.user.dto.request.UserProfileRequest;
import com.zerobase.plistbackend.module.user.dto.response.HostPlaytimeResponse;
import com.zerobase.plistbackend.module.user.dto.response.OtherProfileResponse;
import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;
import com.zerobase.plistbackend.module.user.dto.response.UserPlaytimeResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.model.auth.UserDetail;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.UserErrorStatus;
import com.zerobase.plistbackend.module.user.type.UserRole;
import com.zerobase.plistbackend.module.user.util.S3Util;
import com.zerobase.plistbackend.module.user.util.TimeValueFormatter;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final ParticipantRepository participantRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final S3Util s3Util;
  private final SubscribeRepository subscribeRepository;

  @Override
  @Transactional(readOnly = true)
  public ProfileResponse findProfile(String email) {
    log.info("Find Profile Request email: {}", email);
    User user = userRepository.findByUserEmail(email);
    int followers = subscribeRepository.countByFollower(user);
    return ProfileResponse.createProfileResponse(user, followers);
  }

  @Override
  @Transactional(readOnly = true)
  public OtherProfileResponse findUserIdProfile(Long userId) {

    log.info("Find Profile Request userId: {}", userId);

    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new UserException(UserErrorStatus.USER_NOT_FOUND));

    int followerCount = subscribeRepository.countByFollower(user);
    boolean isLive = channelRepository.existsStatusChannelByUserId(userId,
        ChannelStatus.CHANNEL_STATUS_ACTIVE);
    boolean isSubscribe = isSubscribe(user);
    return OtherProfileResponse.createOtherProfileResponse(user, followerCount, isSubscribe, isLive);
  }

  boolean isSubscribe(User follower) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return false;
    }
    CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    User followee = userRepository.findByUserId(customOAuth2User.findId())
        .orElseThrow(() -> new UserException(UserErrorStatus.USER_NOT_FOUND));
    return subscribeRepository.existsByFolloweeAndFollower(followee, follower);
  }

  @Override
  @Transactional
  public void withdrawUser(Long userId) {
    log.info("Withdraw Request userId: {}", userId);
    User user = findUser(userId);
    user.updateRole(UserRole.ROLE_NONE);
  }

  @Override
  @Transactional
  public ProfileResponse editProfile(UserProfileRequest request, Long userId) {

    log.info("updateProfile Request userId: {}", userId);
    User user = findUser(userId);

    if (request.nickname() != null) {
      user.updateUserName(request.nickname());
    }
    if (request.image() != null) {
      user.updateImage(s3Util.putImage(request.image(), user.getUserEmail()));
    }
    return ProfileResponse.createProfileResponse(user, subscribeRepository.countByFollower(user));
  }

  private User findUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserException(UserErrorStatus.USER_NOT_FOUND));
  }

  @Override
  @Transactional(readOnly = true)
  public HostPlaytimeResponse getHistoryOfHost(Long hostId, int year) {
    List<Channel> channels = channelRepository.findByChannelHostId(
        hostId,
        LocalDate.of(year, 1, 1).atStartOfDay(),
        LocalDate.of(year + 1, 1, 1).atStartOfDay(),
        ChannelStatus.CHANNEL_STATUS_CLOSED
    );

    return HostPlaytimeResponse.builder()
        .totalPlayTime(
            TimeValueFormatter.formatToString(
                channels.stream()
                    .mapToLong(Channel::getTotalPlaytimeOfSeconds)
                    .sum())
        )
        .totalParticipant(
            channels.stream().
                mapToInt(Channel::getChannelLastParticipantCount)
                .sum())
        .totalFollowers(0L)  // 팔로워 기능 추가 예정
        .build();
  }

  public UserPlaytimeResponse getHistoryOfUser(Long userId, int year) {
    long totalSeconds = participantRepository.findByUserIdAndDate(userId,
            LocalDate.of(year, 1, 1).atStartOfDay(),
            LocalDate.of(year + 1, 1, 1).atStartOfDay())
        .stream()
        .mapToLong(Participant::getTotalPlaytimeOfSeconds)
        .sum();
    return UserPlaytimeResponse.from(TimeValueFormatter.formatToString(totalSeconds));

  }
}
