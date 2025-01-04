package com.zerobase.plistbackend.module.userplaylist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.model.auth.UserDetail;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.userplaylist.dto.request.CreateUserPlaylistRequest;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import com.zerobase.plistbackend.module.userplaylist.repository.UserPlaylistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class UserPlaylistServiceImplTest {

  @Mock
  UserPlaylistRepository userPlaylistRepository;

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserPlaylistServiceImpl userPlaylistService;


  @Test
  @DisplayName("유저 플레이리스트 생성 시 제목 입력 -> DB에 저장")
  void createUserPlaylistName() {
    //given
    String userEmail = "testEmail@zerobase.com";
    CreateUserPlaylistRequest req = new CreateUserPlaylistRequest("재즈 목록");
    User user = User.builder().userEmail(userEmail).build();
    CustomOAuth2User customOAuth2User = new CustomOAuth2User(UserDetail.builder()
        .email(userEmail)
        .build());
    given(userRepository.findByUserEmail(anyString())).willReturn(user);

    //when
    userPlaylistService.createUserPlayListName(customOAuth2User, req);

    //then
    then(userRepository).should(times(1)).findByUserEmail(anyString());
    then(userPlaylistRepository).should(times(1)).save(any(UserPlaylist.class));
    assertEquals(userEmail, user.getUserEmail());
  }
}