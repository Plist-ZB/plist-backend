package com.zerobase.plistbackend.module.channel.entity;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.playlist.entity.Playlist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "channel_id")
  private Long channelId;

  @Column(name = "channel_name", length = 100, nullable = false)
  private String channelName;

  @Column(name = "channel_category", length = 50, nullable = false)
  private String channelCategory;

  @Column(name = "channel_thumbnail", nullable = false)
  private String channelThumbnail;

  @CreatedDate
  @Column(name = "channel_created_at", nullable = false)
  private Timestamp channelCreatedAt;

  @Column(name = "channel_finished_at")
  private Timestamp channelFinishedAt;

  @Column(name = "channel_capacity", nullable = false)
  private Long channelCapacity;

  @OneToMany(mappedBy = "channel")
  private List<Playlist> channelPlaylists = new ArrayList<>();

  @OneToMany(mappedBy = "channel")
  private List<Participant> channelParticipants = new ArrayList<>();

  public static Channel createChannel(ChannelRequest request) {
    Date date = new Date();
    return Channel.builder()
        .channelName(request.getChannelName())
        .channelCategory(request.getChannelCategory())
        .channelThumbnail(request.getChannelThumbnail())
        .channelCapacity(request.getChannelCapacity())
        .build();
  }

  public static Channel closeChannel(Channel channel) {
    Date date = new Date();
    channel.channelFinishedAt = new Timestamp(date.getTime());
    return channel;
  }
}
