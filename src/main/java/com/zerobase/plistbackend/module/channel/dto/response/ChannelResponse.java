package com.zerobase.plistbackend.module.channel.dto.response;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.playlist.entity.Playlist;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelResponse {

  private Long channelId;
  private String channelName;
  private String channelCategory;
  private String channelThumbnail;
  private Timestamp channelCreatedAt;
  private Timestamp channelFinishedAt;
  private Long channelCapacity;
  private List<Playlist> channelPlaylists;
  private List<Participant> channelParticipants;


  public static ChannelResponse createChannelResponse(Channel channel) {
    return ChannelResponse.builder()
        .channelId(channel.getChannelId())
        .channelName(channel.getChannelName())
        .channelCategory(channel.getChannelCategory())
        .channelThumbnail(channel.getChannelThumbnail())
        .channelCreatedAt(channel.getChannelCreatedAt())
        .channelFinishedAt(channel.getChannelFinishedAt())
        .channelCapacity(channel.getChannelCapacity())
        .channelPlaylists(channel.getChannelPlaylists())
        .channelParticipants(channel.getChannelParticipants())
        .build();
  }
}
