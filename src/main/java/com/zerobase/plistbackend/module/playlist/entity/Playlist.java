package com.zerobase.plistbackend.module.playlist.entity;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.playlist.util.PlaylistVideoConverter;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import com.zerobase.plistbackend.module.userplaylist.model.Video;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "playlist")
@NoArgsConstructor
public class Playlist {

  @Id
  @Column(name = "playlist_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long playlistId;

  @OneToOne
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @Lob
  @Column(name = "video", columnDefinition = "LONGTEXT")
  @Convert(converter = PlaylistVideoConverter.class)
  private List<Video> videoList;

  public static Playlist from(UserPlaylist userPlaylist) {
    return Playlist.builder()
        .videoList(userPlaylist.getVideoList())
        .build();
  }
}
