package com.zerobase.plistbackend.module.userplaylist.entity;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.home.model.Video;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.UserPlaylistRequest;
import com.zerobase.plistbackend.module.userplaylist.util.UserPlaylistVideoConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "userplaylist")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPlaylist {

  @Id
  @Column(name = "userplaylist_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userPlaylistId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Setter
  @Column(name = "userplaylist_name", length = 50)
  private String userPlaylistName;

  @Lob
  @Setter
  @Builder.Default
  @Column(name = "video", columnDefinition = "LONGTEXT")
  @Convert(converter = UserPlaylistVideoConverter.class)
  private List<Video> videoList = new ArrayList<>();

  public static UserPlaylist createUserPlaylist(User user,
      UserPlaylistRequest userPlaylistRequest) {
    return UserPlaylist.builder()
        .user(user)
        .userPlaylistName(userPlaylistRequest.getUserPlaylistName())
        .build();
  }

  public static UserPlaylist fromChannelPlaylist(User user, Channel channel) {
    String userPlaylistName = "(" + channel.getChannelHost().getUserName() + ")" + channel.getChannelName() + "_"
        + channel.getChannelId();

    return UserPlaylist.builder()
        .user(user)
        .userPlaylistName(userPlaylistName)
        .videoList(channel.getChannelPlaylist().getVideoList())
        .build();
  }
}
