package com.zerobase.plistbackend.module.playlist.entity;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.home.model.Video;
import com.zerobase.plistbackend.module.playlist.util.PlaylistVideoConverter;
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
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
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

  @Setter
  @Lob
  @Builder.Default
  @Column(name = "video", columnDefinition = "LONGTEXT")
  @Convert(converter = PlaylistVideoConverter.class)
  private List<Video> videoList = new ArrayList<>();

  public void connectChannel(Channel channel) {
    this.channel = channel;
  }
}
