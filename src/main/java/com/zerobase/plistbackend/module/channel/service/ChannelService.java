package com.zerobase.plistbackend.module.channel.service;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.DetailChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.DetailClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChannelService {

  DetailChannelResponse addChannel(CustomOAuth2User customOAuth2User, ChannelRequest channelRequest);

  Slice<StreamingChannelResponse> findChannelList(Long cursorId, Pageable pageable);

  List<StreamingChannelResponse> searchChannel(String searchValue);

  Slice<StreamingChannelResponse> findChannelFromChannelCategory(Long categoryId, Long cursorId, Long cursorPopular, Pageable pageable);

  DetailChannelResponse userEnterChannel(CustomOAuth2User customOAuth2User, Long channelId);

  void userExitChannel(CustomOAuth2User customOAuth2User, Long channelId);

  void hostExitChannel(CustomOAuth2User customOAuth2User, Long channelId);

  void addVideoToChannel(Long channelId, VideoRequest videoRequest,
      CustomOAuth2User customOAuth2User);

  void deleteVideoToChannel(Long channelId, Long id, CustomOAuth2User customOAuth2User);

  void savePlaylistToUserPlaylist(Long channelId, CustomOAuth2User customOAuth2User);

  DetailChannelResponse findOneChannel(Long channelId, CustomOAuth2User customOAuth2User);

  void updateChannelPlaylist(Long channelId, String updateChannelPlaylistJson, CustomOAuth2User customOAuth2User);

  Slice<StreamingChannelResponse> findChannelListPopular(Long cursorId, Long cursorPopular, Pageable pageable);

  Slice<ClosedChannelResponse> findUserChannelHistory(CustomOAuth2User customOAuth2User, Long cursorId, Pageable pageable);

  void likeVideo(CustomOAuth2User customOAuth2User, VideoRequest videoRequest);

  DetailClosedChannelResponse findOneUserChannelHistory(CustomOAuth2User customOAuth2User,
      Long channelId);
}
