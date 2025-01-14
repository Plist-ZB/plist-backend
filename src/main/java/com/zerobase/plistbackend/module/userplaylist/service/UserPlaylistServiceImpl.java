package com.zerobase.plistbackend.module.userplaylist.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.home.exception.VideoException;
import com.zerobase.plistbackend.module.home.model.Video;
import com.zerobase.plistbackend.module.home.type.VideoErrorStatus;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.userplaylist.dto.request.UserPlaylistRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.response.DetailUserPlaylistResponse;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import com.zerobase.plistbackend.module.userplaylist.exception.UserPlaylistException;
import com.zerobase.plistbackend.module.userplaylist.repository.UserPlaylistRepository;
import com.zerobase.plistbackend.module.userplaylist.type.UserPlaylistErrorStatus;
import com.zerobase.plistbackend.module.userplaylist.util.UserPlaylistVideoConverter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPlaylistServiceImpl implements UserPlaylistService {

  private final UserPlaylistRepository userPlaylistRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public void createUserPlayList(UserPlaylistRequest userPlaylistRequest,
      CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    if (userPlaylistRepository.existsByUserAndUserPlaylistName(user,
        userPlaylistRequest.getUserPlaylistName())) {
      throw new UserPlaylistException(UserPlaylistErrorStatus.ALREADY_EXIST);
    }
    UserPlaylist userPlaylist = UserPlaylist.createUserPlaylist(user, userPlaylistRequest);

    userPlaylistRepository.save(userPlaylist);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserPlaylistResponse> findAllUserPlaylist(CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    List<UserPlaylist> userPlaylists = userPlaylistRepository.findByUser(user);

    return userPlaylists.stream().map(UserPlaylistResponse::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public DetailUserPlaylistResponse findOneUserPlaylist(Long userPlaylistId,
      CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistId(user,
            userPlaylistId)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));

    return DetailUserPlaylistResponse.fromEntity(userPlaylist);
  }

  @Override
  @Transactional
  public void deleteUserPlaylist(Long userPlaylistId) {

    UserPlaylist userPlaylist = userPlaylistRepository.findById(userPlaylistId)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));

    if (userPlaylist.getUserPlaylistName().equals("favorite")) {
      throw new UserPlaylistException(UserPlaylistErrorStatus.CANT_DELETE_FAVORITE);
    }

    userPlaylistRepository.deleteById(userPlaylistId);
  }

  @Override
  @Transactional
  public void addVideo(CustomOAuth2User customOAuth2User, Long userPlaylistId,
      VideoRequest videoRequest) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistId(user,
            userPlaylistId)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));

    Video video = Video.createVideo(videoRequest, userPlaylist.getVideoList());

    userPlaylist.getVideoList().add(video);

    userPlaylistRepository.save(userPlaylist);
  }

  @Override
  @Transactional
  public void removeVideo(CustomOAuth2User customOAuth2User, Long userPlaylistId,
      Long id) {
    //1. 현재 로그인 중인 유저의 정보 받아오기
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistId(user,
            userPlaylistId)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));

    List<Video> videoList = userPlaylist.getVideoList();

    Video video = videoList.stream().filter(it -> it.getId().equals(id)).findFirst()
        .orElseThrow(() -> new VideoException(VideoErrorStatus.NOT_EXIST));
    videoList.remove(video);

    userPlaylistRepository.save(userPlaylist);
  }

  @Override
  @Transactional
  public void updateUserPlaylist(Long userPlaylistId,
      String updateUserPlaylistJson, CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistId(user,
            userPlaylistId)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));

    UserPlaylistVideoConverter userPlaylistVideoConverter = new UserPlaylistVideoConverter(
        new ObjectMapper());
    List<Video> videoList = userPlaylistVideoConverter.convertToEntityAttribute(
        updateUserPlaylistJson);

    userPlaylist.setVideoList(videoList);

    userPlaylistRepository.save(userPlaylist);
  }

  @Override
  @Transactional
  public void changeUserPlaylistName(CustomOAuth2User customOAuth2User,
      UserPlaylistRequest userPlaylistRequest, Long userPlaylistId) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    UserPlaylist userPlaylist = userPlaylistRepository.findById(userPlaylistId)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));

    if (userPlaylist.getUserPlaylistName().equals("favorite")) {
      throw new UserPlaylistException(UserPlaylistErrorStatus.CANT_UPDATE_FAVORITE);
    }

    if (userPlaylistRepository.existsByUserAndUserPlaylistName(user,
        userPlaylistRequest.getUserPlaylistName())) {
      throw new UserPlaylistException(UserPlaylistErrorStatus.ALREADY_EXIST);
    }

    userPlaylist.setUserPlaylistName(userPlaylistRequest.getUserPlaylistName());

    userPlaylistRepository.save(userPlaylist);
  }
}
