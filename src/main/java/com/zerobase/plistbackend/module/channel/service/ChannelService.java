package com.zerobase.plistbackend.module.channel.service;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ChannelResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import java.util.List;

public interface ChannelService {

  ChannelResponse addChannel(CustomOAuth2User customOAuth2User, ChannelRequest channelRequest);

  List<ChannelResponse> findChannelList();

  List<ChannelResponse> findChannelFromChannelName(String channelName);

  List<ChannelResponse> findChannelFromChannelCategory(String channelCategory);

  void enterChannel(CustomOAuth2User customOAuth2User, Long channelId);

  void userExitChannel(CustomOAuth2User customOAuth2User, Long channelId);

  void hostExitChannel(CustomOAuth2User customOAuth2User, Long channelId);

  ChannelResponse addVideoToChannel(Long channelId, VideoRequest videoRequest, CustomOAuth2User customOAuth2User);

  ChannelResponse deleteVideoToChannel(Long channelId, Long id, CustomOAuth2User customOAuth2User);

  UserPlaylistResponse savePlaylistToUserPlaylist(Long channelId, CustomOAuth2User customOAuth2User);

//
//  Object addVideoToPlaylistItem();
//
//  Object deletePlaylistItem();
//
//  Object playVideo();
//
//  Object pauseVideo();
//
//  Object stopVideo();
//
//  Object nextVideo();
//
//  Object previousVideo();
//
//  Object findVideoDetail();
//
//  Object createPlaylist();


}
