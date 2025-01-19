package com.zerobase.plistbackend.module.user.service;

import com.zerobase.plistbackend.module.user.dto.request.UserProfileRequest;
import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.UserErrorStatus;
import com.zerobase.plistbackend.module.user.type.UserRole;
import com.zerobase.plistbackend.module.user.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final S3Util s3Util;

  @Override
  @Transactional(readOnly = true)
  public ProfileResponse findProfile(String email) {
    log.info("Find Profile Request email: {}", email);
    return userRepository.findProfileByEmail(email);
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

    if(request.nickname() != null) {
      user.updateUserName(request.nickname());
    }
    if(request.image() != null) {
      user.updateImage(s3Util.putImage(request.image(), user.getUserEmail()));
    }
    return new ProfileResponse(user.getUserEmail(), user.getUserName(), user.getUserImage());
  }

  private User findUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserException(UserErrorStatus.USER_NOT_FOUND));
  }
}
