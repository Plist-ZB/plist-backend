package com.zerobase.plistbackend.module.channel.entity;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.playlist.entity.Playlist;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Channel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "channel_id")
  private Long channelId;

  @Column(name = "channel_name", length = 50, nullable = false)
  private String channelName;

  @Column(name = "channel_category", length = 50, nullable = false)
  private String channelCategory;

  @Column(name = "channel_thumbnail", nullable = false)
  private String channelThumbnail;

  @CreatedDate
  @Column(name = "channel_created_at")
  private Timestamp channelCreatedAt;

  @Column(name = "channel_finished_at")
  private Timestamp channelFinishedAt;

  @Column(name = "channel_capacity", nullable = false)
  private Long channelCapacity;

  @Column(name = "channel_status", nullable = false)
  private ChannelStatus channelStatus;

  @OneToOne(mappedBy = "channel", cascade = CascadeType.ALL)
  private Playlist channelPlaylist;

  @OneToMany(mappedBy = "channel")
  private List<Participant> channelParticipants;

  public static Channel createChannel(ChannelRequest request, Playlist playlist) {
    return Channel.builder()
        .channelName(request.getChannelName())
        .channelCategory(request.getChannelCategory())
        .channelThumbnail(request.getChannelThumbnail())
        .channelCapacity(request.getChannelCapacity())
        .channelParticipants(new ArrayList<>())
        .channelPlaylist(playlist)
        .channelStatus(ChannelStatus.CHANNEL_STATUS_ACTIVE)
        .build();
  }

  public static void closeChannel(Channel channel) {
    Date date = new Date();
    channel.channelFinishedAt = new Timestamp(date.getTime());
    channel.channelStatus = ChannelStatus.CHANNEL_STATUS_CLOSED;
  }
}
