package com.zerobase.plistbackend.module.channel.dto.response;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.home.model.Video;
import com.zerobase.plistbackend.module.user.entity.User;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailChannelResponse {

  private Long channelId;
  private String channelName;
  private String channelCreatedAt;
  private List<Video> videoList;
  private boolean isHost;

  public static DetailChannelResponse createDetailChannelResponse(Channel channel, User user) {

    return DetailChannelResponse.builder()
        .channelId(channel.getChannelId())
        .channelName(channel.getChannelName())
        .channelCreatedAt(convertStringFormat(channel.getChannelCreatedAt()))
        .videoList(channel.getChannelPlaylist().getVideoList())
        .isHost(user.getParticipant().getIsHost())
        .build();
  }

  public static String convertStringFormat(Timestamp channelCreatedAt) {
    LocalDateTime localDateTime = channelCreatedAt.toLocalDateTime();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    return localDateTime.format(formatter);
  }

}
