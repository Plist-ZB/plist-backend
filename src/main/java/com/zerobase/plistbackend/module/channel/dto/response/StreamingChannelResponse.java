package com.zerobase.plistbackend.module.channel.dto.response;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.playlist.dto.response.PlaylistResponse;
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
  private ChannelStatus channelStatus;
  private String channelHost;
  private int channelParticipantCount;
  private PlaylistResponse playlistResponse;
  //  private Long channelCapacity;


  public static StreamingChannelResponse createStreamingChannelResponse(Channel channel) {
    PlaylistResponse playlistResponse = PlaylistResponse.from(channel.getChannelPlaylist());

    return StreamingChannelResponse.builder()
        .channelId(channel.getChannelId())
        .channelName(channel.getChannelName())
        .channelCategoryName(channel.getCategory().getCategoryName())
        .channelThumbnail(channel.getChannelThumbnail())
        .channelStreamingTime(streamingTime(channel))
        .channelStatus(channel.getChannelStatus())
        .channelHost(channel.getChannelHost())
        .channelParticipantCount(channel.getChannelParticipants().size())
        .playlistResponse(playlistResponse)
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
