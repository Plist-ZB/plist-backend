package com.zerobase.plistbackend.module.channel.dto.response;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import java.time.Duration;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClosedChannelResponse {

  private Long channelId;
  private String channelName;
  private String channelCategoryName;
  private String channelThumbnail;
  private String channelDurationTime;
  private String channelHost;
  private int channelLastParticipantCount;
  //  private Long channelCapacity;


  public static ClosedChannelResponse createClosedChannelResponse(Channel channel) {
    String thumbnail = "";
    if (!channel.getChannelPlaylist().getVideoList().isEmpty()) {
      thumbnail = channel.getChannelPlaylist().getVideoList().get(0).getVideoThumbnail();
    }

    return ClosedChannelResponse.builder()
        .channelId(channel.getChannelId())
        .channelName(channel.getChannelName())
        .channelCategoryName(channel.getCategory().getCategoryName())
        .channelThumbnail(thumbnail)
        .channelDurationTime(durationTime(channel))
        .channelHost(channel.getChannelHost())
        .channelLastParticipantCount(channel.getChannelLastParticipantCount())
//        .channelCapacity(channel.getChannelCapacity())
        .build();
  }

  public static String durationTime(Channel channel) {
    Duration duration = Duration.ofMillis(
        channel.getChannelFinishedAt().getTime() - channel.getChannelCreatedAt().getTime());

    long hours = duration.toHours();
    long minutes = duration.toMinutes() % 60;

    return String.format("%d시간%d분", hours, minutes);
  }
}
