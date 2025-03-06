package com.zerobase.plistbackend.module.channel.util;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.home.model.Video;
import com.zerobase.plistbackend.module.user.entity.User;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChannelResponseMapper {

  private ChannelResponseMapper() {
    throw new AssertionError("ResponseUtil 객체 생성 금지");
  }

  public static String findThumbnail(List<Video> videoList) {
    if (!videoList.isEmpty()) {
      return videoList.get(0).getVideoThumbnail();
    }
    return "";
  }

  public static String durationTime(Timestamp channelCreatedAt, Timestamp channelFinishedAt) {
    Duration duration = Duration.ofMillis(
        channelFinishedAt.getTime() - channelCreatedAt.getTime());

    long hours = duration.toHours();
    long minutes = duration.toMinutes() % 60;

    return String.format("%d시간%d분", hours, minutes);
  }

  public static String convertStringFormat(Timestamp channelCreatedAt) {
    LocalDateTime localDateTime = channelCreatedAt.toLocalDateTime();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    return localDateTime.format(formatter);
  }

  public static String streamingTime(Timestamp channelCreatedAt) {
    Duration duration = Duration.ofMillis(
        System.currentTimeMillis() - channelCreatedAt.getTime());

    long hours = duration.toHours();
    long minutes = duration.toMinutes() % 60;

    return String.format("%d시간%d분", hours, minutes);
  }

  //삭제 예정
  public static Boolean isChannelHost(Channel channel, User user) {
    return channel.getChannelHost().equals(user);
  }

}
