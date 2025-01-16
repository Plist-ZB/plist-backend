package com.zerobase.plistbackend.module.channel.service;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.DetailChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.DetailClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import java.util.List;

public interface ChannelService {

  void addChannel(CustomOAuth2User customOAuth2User, ChannelRequest channelRequest);

  List<StreamingChannelResponse> findChannelList();

  List<StreamingChannelResponse> searchChannel(String searchValue);

  List<StreamingChannelResponse> findChannelFromChannelCategory(Long categoryId);

  void userEnterChannel(CustomOAuth2User customOAuth2User, Long channelId);

  void userExitChannel(CustomOAuth2User customOAuth2User, Long channelId);

  void hostExitChannel(CustomOAuth2User customOAuth2User, Long channelId);

  void addVideoToChannel(Long channelId, VideoRequest videoRequest,
      CustomOAuth2User customOAuth2User);

  void deleteVideoToChannel(Long channelId, Long id, CustomOAuth2User customOAuth2User);

  void savePlaylistToUserPlaylist(Long channelId, CustomOAuth2User customOAuth2User);

  DetailChannelResponse findOneChannel(Long channelId);

  void updateChannelPlaylist(Long channelId, String updateChannelPlaylistJson, CustomOAuth2User customOAuth2User);

  List<StreamingChannelResponse> findChannelListPopular();

  List<ClosedChannelResponse> findUserChannelHistory(CustomOAuth2User customOAuth2User);

  void likeVideo(CustomOAuth2User customOAuth2User, VideoRequest videoRequest);

  DetailClosedChannelResponse findOneUserChannelHistory(CustomOAuth2User customOAuth2User,
      Long channelId);
}
