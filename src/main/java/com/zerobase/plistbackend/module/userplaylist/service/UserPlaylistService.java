package com.zerobase.plistbackend.module.userplaylist.service;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import java.util.List;

public interface UserPlaylistService {

  UserPlaylistResponse createUserPlayList(String userPlaylistName, CustomOAuth2User customOAuth2User);

  UserPlaylistResponse findOneUserPlaylist(Long userPlaylistId, CustomOAuth2User customOAuth2User);

  List<UserPlaylistResponse> findAllUserPlaylist(CustomOAuth2User customOAuth2User);

  UserPlaylistResponse addVideo(CustomOAuth2User customOAuth2User, Long userPlaylistId, VideoRequest videoRequest);

  UserPlaylistResponse removeVideo(CustomOAuth2User customOAuth2User, Long userPlaylistId, Long id);

  void deleteUserPlaylist(Long userPlaylistId, CustomOAuth2User customOAuth2User);

}
