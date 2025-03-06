package com.zerobase.plistbackend.module.channel.dto.response;

import com.zerobase.plistbackend.module.channel.util.ChannelResponseMapper;
import com.zerobase.plistbackend.module.home.model.Video;
import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;

@Getter
public class ClosedChannelResponse {

  private final Long channelId;
  private final String channelName;
  private final String channelCategoryName;
  private final String channelThumbnail;
  private final String channelCreatedAt;
  private final String channelDurationTime;
  private final String channelHostName;
  private final int channelLastParticipantCount;
  //  private Long channelCapacity;


  public ClosedChannelResponse(Long channelId, String channelName, String channelCategoryName,
      List<Video> videoList, Timestamp channelCreatedAt, Timestamp channelFinishedAt,
      String channelHostName, int channelLastParticipantCount) {
    this.channelId = channelId;
    this.channelName = channelName;
    this.channelCategoryName = channelCategoryName;
    this.channelThumbnail = ChannelResponseMapper.findThumbnail(videoList);
    this.channelCreatedAt = ChannelResponseMapper.convertStringFormat(channelCreatedAt);
    this.channelDurationTime = ChannelResponseMapper.durationTime(channelCreatedAt,
        channelFinishedAt);
    this.channelHostName = channelHostName;
    this.channelLastParticipantCount = channelLastParticipantCount;
  }

}
