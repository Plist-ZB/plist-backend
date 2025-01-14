package com.zerobase.plistbackend.module.channel.dto.response;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.playlist.dto.response.PlaylistResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailChannelResponse {

  private Long channelId;
  private String channelName;
  private String channelCreatedAt;
  private PlaylistResponse channelPlaylist;

  public static DetailChannelResponse createDetailChannelResponse(Channel channel) {
    PlaylistResponse playlistResponse = PlaylistResponse.from(channel.getChannelPlaylist());

    return DetailChannelResponse.builder()
        .channelId(channel.getChannelId())
        .channelName(channel.getChannelName())
        .channelCreatedAt(convertStringFormat(channel.getChannelCreatedAt()))
        .channelPlaylist(playlistResponse)
        .build();
  }

  public static String convertStringFormat(Timestamp channelCreatedAt) {
    LocalDateTime localDateTime = channelCreatedAt.toLocalDateTime();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    return localDateTime.format(formatter);
  }

}
