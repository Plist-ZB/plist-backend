package com.zerobase.plistbackend.module.userplaylist.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.home.model.Video;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.userplaylist.dto.request.UserPlaylistRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
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

  public UserPlaylistResponse createUserPlayList(UserPlaylistRequest userPlaylistRequest,
      CustomOAuth2User customOAuth2User) {
    //1. 현재 로그인 중인 유저의 정보 받아오기
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    //2. 유저플레이리스트네임으로 유저플레이리스트 생성 (Video 는 빈배열로 생성)
    if (userPlaylistRepository.existsByUserAndUserPlaylistName(user,
        userPlaylistRequest.getUserPlaylistName())) {
      throw new UserPlaylistException(UserPlaylistErrorStatus.ALREADY_EXIST);
    }
    UserPlaylist userPlaylist = UserPlaylist.createUserPlaylist(user, userPlaylistRequest);
    userPlaylistRepository.save(userPlaylist);

    //3. 생성된 유저플레이리스트 반환.
    return UserPlaylistResponse.fromEntity(userPlaylist);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserPlaylistResponse> findAllUserPlaylist(CustomOAuth2User customOAuth2User) {
    //1. 현재 로그인 중인 유저의 정보 받아오기
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    //2. 유저 정보를 통해 유저플레이리스트 조회
    List<UserPlaylist> userPlaylists = userPlaylistRepository.findByUser(user);
    //3. List<userPlaylistResponse>로 반환.
    return userPlaylists.stream().map(UserPlaylistResponse::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public UserPlaylistResponse findOneUserPlaylist(Long userPlaylistId,
      CustomOAuth2User customOAuth2User) {
    //1. 현재 로그인 중인 유저의 정보 받아오기
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    //2. 유저 플레이리스트ID 와 유저 정보를 통해 userPlaylist 가져오기.
    UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistId(user,
            userPlaylistId)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));
    //3. userPlaylistResponse로 반환.
    return UserPlaylistResponse.fromEntity(userPlaylist);
  }

  @Override
  @Transactional
  public void deleteUserPlaylist(Long userPlaylistId, CustomOAuth2User customOAuth2User) {
    //1. 현재 로그인 중인 유저의 정보 받아오기
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    //2. 유저 플레이리스트ID와 유저 정보를 통해 userPlaylist 삭제하기.
    if (userPlaylistRepository.existsByUserAndUserPlaylistId(user, userPlaylistId)) {
      userPlaylistRepository.deleteByUserAndUserPlaylistId(user, userPlaylistId);
    } else {
      throw new RuntimeException("해당 유저플레이리스트가 존재하지 않습니다.");
    }
  }

  @Override
  @Transactional
  public UserPlaylistResponse addVideo(CustomOAuth2User customOAuth2User, Long userPlaylistId,
      VideoRequest videoRequest) {
    //1. 현재 로그인 중인 유저의 정보 받아오기
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    //2. 유저 플레이리스트ID와 유저 정보를 통해 userPlaylist 가져오기
    UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistId(user,
            userPlaylistId)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));
    //3. VideoRequest를 Video 객체로 변환
    Video video = Video.createVideo(videoRequest, userPlaylist.getVideoList());
    //4. userPlaylist 의 VideoList 에 Video 객체 추가
    userPlaylist.getVideoList().add(video);
    userPlaylistRepository.save(userPlaylist);
    //5. userPlaylistResponse로 반환.
    return UserPlaylistResponse.fromEntity(userPlaylist);
  }

  @Override
  @Transactional
  public UserPlaylistResponse removeVideo(CustomOAuth2User customOAuth2User, Long userPlaylistId,
      Long id) {
    //1. 현재 로그인 중인 유저의 정보 받아오기
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    //2. 유저 플레이리스트ID와 유저 정보를 통해 userPlaylist 가져오기
    UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistId(user,
            userPlaylistId)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));
    //3. userPlaylist의 VideoList 가져오기.
    List<Video> videoList = userPlaylist.getVideoList();
    //4. VideoList에서 videoId와 같은 값 제거.
    Video video = videoList.stream().filter(it -> it.getId().equals(id)).findFirst()
        .orElseThrow(() -> new RuntimeException("해당하는 비디오가 없습니다."));
    videoList.remove(video);
    //5. userPlaylist 다시 저장
    userPlaylistRepository.save(userPlaylist);
    //6. userPlaylistResponse로 반환.
    return UserPlaylistResponse.fromEntity(userPlaylist);
  }

  @Override
  public UserPlaylistResponse updateUserPlaylist(Long userPlaylistId,
      String updateUserPlaylistJson, CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistId(user,
            userPlaylistId)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));

    UserPlaylistVideoConverter userPlaylistVideoConverter = new UserPlaylistVideoConverter(new ObjectMapper());
    List<Video> videoList = userPlaylistVideoConverter.convertToEntityAttribute(updateUserPlaylistJson);

    userPlaylist.setVideoList(videoList);
    userPlaylistRepository.save(userPlaylist);

    return UserPlaylistResponse.fromEntity(userPlaylist);
  }
}
