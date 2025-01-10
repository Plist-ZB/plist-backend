package com.zerobase.plistbackend.module.channel.service;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import java.util.List;

public interface ChannelService {

  void addChannel(CustomOAuth2User customOAuth2User, ChannelRequest channelRequest);

  List<StreamingChannelResponse> findChannelList();

  List<StreamingChannelResponse> searchChannel(String searchValue);

  List<StreamingChannelResponse> findChannelFromChannelCategory(Long categoryId);

  void enterChannel(CustomOAuth2User customOAuth2User, Long channelId);

  void userExitChannel(CustomOAuth2User customOAuth2User, Long channelId);

  void hostExitChannel(CustomOAuth2User customOAuth2User, Long channelId);

  StreamingChannelResponse addVideoToChannel(Long channelId, VideoRequest videoRequest, CustomOAuth2User customOAuth2User);

  StreamingChannelResponse deleteVideoToChannel(Long channelId, Long id, CustomOAuth2User customOAuth2User);

  UserPlaylistResponse savePlaylistToUserPlaylist(Long channelId, CustomOAuth2User customOAuth2User);

  StreamingChannelResponse findOneChannel(Long channelId);

  StreamingChannelResponse updateChannelPlaylist(Long channelId, String updateChannelPlaylistJson);

  List<StreamingChannelResponse> findChannelListPopular();

  List<ClosedChannelResponse> findUserChannelHistory(CustomOAuth2User customOAuth2User);
}
