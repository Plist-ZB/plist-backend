package com.zerobase.plistbackend.module.userplaylist.entity;

import com.zerobase.plistbackend.module.playlist.entity.Playlist;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.userplaylist.domain.Video;
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
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
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

  @Column(name = "userplaylist_name", length = 30)
  private String userPlaylistName;

  @Lob
  @Column(name = "video", columnDefinition = "LONGTEXT")
  @Convert(converter = UserPlaylistVideoConverter.class)
  private List<Video> videoList;

  public static UserPlaylist createUserPlaylist(User user, String userPlaylistName) {
    return UserPlaylist.builder()
        .user(user)
        .userPlaylistName(userPlaylistName)
        .videoList(new ArrayList<>())
        .build();
  }

  public static UserPlaylist fromChannelPlaylist (User user, Playlist playlist) {
    String uuid = UUID.randomUUID().toString();
    return UserPlaylist.builder()
        .user(user)
        .userPlaylistName("Playlist_" + uuid)
        .videoList(playlist.getVideoList())
        .build();
  }
}
