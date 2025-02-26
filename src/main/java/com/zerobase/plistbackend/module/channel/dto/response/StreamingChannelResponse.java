package com.zerobase.plistbackend.module.channel.dto.response;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import java.time.Duration;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StreamingChannelResponse {

  private Long channelId;
  private String channelName;
  private String channelCategoryName;
  private String channelThumbnail;
  private String channelStreamingTime;
  private String channelHost;
  private int channelParticipantCount;
  //  private Long channelCapacity;


  public static StreamingChannelResponse createStreamingChannelResponse(Channel channel) {
    String thumbnail = "";
    if (!channel.getChannelPlaylist().getVideoList().isEmpty()) {
      thumbnail = channel.getChannelPlaylist().getVideoList().get(0).getVideoThumbnail();
    }

    return StreamingChannelResponse.builder()
        .channelId(channel.getChannelId())
        .channelName(channel.getChannelName())
        .channelCategoryName(channel.getCategory().getCategoryName())
        .channelThumbnail(thumbnail)
        .channelStreamingTime(streamingTime(channel))
        .channelHost(channel.getChannelHost().getUserName())
        .channelParticipantCount(channel.getChannelParticipants().size())
        .build();
  }

  public static String streamingTime(Channel channel) {
    Duration duration = Duration.ofMillis(
        System.currentTimeMillis() - channel.getChannelCreatedAt().getTime());

    long hours = duration.toHours();
    long minutes = duration.toMinutes() % 60;

    return String.format("%d시간%d분", hours, minutes);
  }
}
