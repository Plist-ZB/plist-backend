package com.zerobase.plistbackend.module.channel.service;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ChannelResponse;
import java.util.List;

public interface ChannelService {

  ChannelResponse addChannel(ChannelRequest channelRequest);

  List<ChannelResponse> findChannelList();

  List<ChannelResponse> findChannelFromChannelName(String channelName);

  List<ChannelResponse> findChannelFromChannelCategory(String channelCategory);

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
