package com.zerobase.plistbackend.module.user.service;

import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public ProfileResponse findProfile(String email) {
    log.info("Find Profile Request email: {}", email);
    User user = userRepository.findByUserEmail(email);
    return new ProfileResponse(email, user.getUserName(), user.getUserImage());
  }

}
