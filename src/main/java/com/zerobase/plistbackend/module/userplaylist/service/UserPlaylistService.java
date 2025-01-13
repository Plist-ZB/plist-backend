package com.zerobase.plistbackend.module.userplaylist.service;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.UserPlaylistRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.response.DetailUserPlaylistResponse;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import java.util.List;

public interface UserPlaylistService {

  void createUserPlayList(UserPlaylistRequest userPlaylistRequest, CustomOAuth2User customOAuth2User);

  DetailUserPlaylistResponse findOneUserPlaylist(Long userPlaylistId, CustomOAuth2User customOAuth2User);

  List<UserPlaylistResponse> findAllUserPlaylist(CustomOAuth2User customOAuth2User);

  void addVideo(CustomOAuth2User customOAuth2User, Long userPlaylistId, VideoRequest videoRequest);

  void removeVideo(CustomOAuth2User customOAuth2User, Long userPlaylistId, Long id);

  void deleteUserPlaylist(Long userPlaylistId, CustomOAuth2User customOAuth2User);

  void updateUserPlaylist(Long userPlaylistId, String updateUserPlaylistJson, CustomOAuth2User customOAuth2User);

  void changeUserPlaylistName(CustomOAuth2User customOAuth2User, UserPlaylistRequest userPlaylistRequest, Long userPlaylistId);
}
