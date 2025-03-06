package com.zerobase.plistbackend.module.channel.dto.response;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.util.ChannelResponseMapper;
import com.zerobase.plistbackend.module.home.model.Video;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailClosedChannelResponse {

  private Long channelId;
  private String channelName;
  private String channelCategoryName;
  private String channelThumbnail;
  private String channelCreatedAt;
  private String channelDurationTime;
  private String channelHostName;
  private List<Video> videoList;
  private int channelLastParticipantCount;
  //  private Long channelCapacity;


  public static DetailClosedChannelResponse createClosedChannelResponse(Channel channel) {

    return DetailClosedChannelResponse.builder()
        .channelId(channel.getChannelId())
        .channelName(channel.getChannelName())
        .channelCategoryName(channel.getCategory().getCategoryName())
        .channelThumbnail(
            ChannelResponseMapper.findThumbnail(channel.getChannelPlaylist().getVideoList()))
        .channelCreatedAt(ChannelResponseMapper.convertStringFormat(channel.getChannelCreatedAt()))
        .channelDurationTime(ChannelResponseMapper.durationTime(channel.getChannelCreatedAt(),
            channel.getChannelFinishedAt()))
        .channelHostName(channel.getChannelHost().getUserName())
        .videoList(channel.getChannelPlaylist().getVideoList())
        .channelLastParticipantCount(channel.getChannelLastParticipantCount())
//        .channelCapacity(channel.getChannelCapacity())
        .build();
  }
}
