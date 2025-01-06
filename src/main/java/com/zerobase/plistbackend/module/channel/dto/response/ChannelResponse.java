package com.zerobase.plistbackend.module.channel.dto.response;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.participant.dto.response.ParticipantResponse;
import com.zerobase.plistbackend.module.playlist.dto.response.PlaylistResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
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
  private ChannelStatus channelStatus;
  private PlaylistResponse channelPlaylist;
  private List<ParticipantResponse> channelParticipants;


  public static ChannelResponse createChannelResponse(Channel channel,
      PlaylistResponse playlistResponse,
      List<ParticipantResponse> participantResponseList) {
    return ChannelResponse.builder()
        .channelId(channel.getChannelId())
        .channelName(channel.getChannelName())
        .channelCategory(channel.getChannelCategory())
        .channelThumbnail(channel.getChannelThumbnail())
        .channelCreatedAt(channel.getChannelCreatedAt())
        .channelFinishedAt(channel.getChannelFinishedAt())
        .channelCapacity(channel.getChannelCapacity())
        .channelStatus(channel.getChannelStatus())
        .channelPlaylist(playlistResponse)
        .channelParticipants(participantResponseList)
        .build();
  }

  public static List<ChannelResponse> createChannelResponseList(List<Channel> channelList) {
    List<ChannelResponse> channelResponseList = new ArrayList<>();

    for (Channel channel : channelList) {
      List<ParticipantResponse> participantResponseList = channel.getChannelParticipants().stream()
          .map(
              ParticipantResponse::createParticipantResponse).toList();
      PlaylistResponse playlistResponse = PlaylistResponse.from(channel.getChannelPlaylist());
      ChannelResponse channelResponse = ChannelResponse.createChannelResponse(channel,
          playlistResponse,
          participantResponseList);
      channelResponseList.add(channelResponse);
    }

    return channelResponseList;
  }
}
