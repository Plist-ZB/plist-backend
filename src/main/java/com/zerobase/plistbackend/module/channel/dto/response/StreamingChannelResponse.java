package com.zerobase.plistbackend.module.channel.dto.response;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.util.ChannelResponseMapper;
import com.zerobase.plistbackend.module.home.model.Video;
import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;

@Getter
public class StreamingChannelResponse {

  private final Long channelId;
  private final String channelName;
  private final String channelCategoryName;
  private final String channelThumbnail;
  private final String channelStreamingTime;
  private final String channelHostName;
  private final Long channelParticipantCount;
  //  private Long channelCapacity;

  public StreamingChannelResponse(Long channelId, String channelName, String channelCategoryName,
      List<Video> videoList, Timestamp channelCreatedAt, String channelHostName,
      Long channelParticipantCount) {
    this.channelId = channelId;
    this.channelName = channelName;
    this.channelCategoryName = channelCategoryName;
    this.channelThumbnail = ChannelResponseMapper.findThumbnail(videoList);
    this.channelStreamingTime = ChannelResponseMapper.streamingTime(channelCreatedAt);
    this.channelHostName = channelHostName;
    this.channelParticipantCount = channelParticipantCount;
  }

  public static StreamingChannelResponse createStreamingChannelResponse(Channel channel) {

    return new StreamingChannelResponse(
        channel.getChannelId(),
        channel.getChannelName(),
        channel.getCategory().getCategoryName(),
        channel.getChannelPlaylist().getVideoList(),
        channel.getChannelCreatedAt(),
        channel.getChannelHost().getUserName(),
        (long) channel.getChannelParticipants().size());
  }
}
