package com.zerobase.plistbackend.module.playlist.entity;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.playlist.util.PlaylistVideoConverter;
import com.zerobase.plistbackend.module.userplaylist.domain.Video;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "playlist")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Playlist {

  @Id
  @Column(name = "playlist_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long playlistId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @Lob
  @Column(name = "video")
  @Convert(converter = PlaylistVideoConverter.class)
  private Video video;
}
