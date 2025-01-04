package com.zerobase.plistbackend.module.userplaylist.service;

import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.userplaylist.dto.request.CreateUserPlaylistRequest;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import com.zerobase.plistbackend.module.userplaylist.repository.UserPlaylistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPlaylistServiceImpl implements UserPlaylistService {

  private final UserPlaylistRepository userPlaylistRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public void createUserPlayListName(CustomOAuth2User user, CreateUserPlaylistRequest userPlaylistName) {
    User findUser = userRepository.findByUserEmail(user.findEmail());
    userPlaylistRepository.save(
        UserPlaylist.builder()
            .user(findUser)
            .userPlaylistName(userPlaylistName.getUserPlaylistName())
            .build()
    );
  }
}
