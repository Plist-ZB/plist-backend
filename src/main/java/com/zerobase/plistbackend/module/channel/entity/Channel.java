package com.zerobase.plistbackend.module.channel.entity;

import com.zerobase.plistbackend.module.category.entity.Category;
import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.playlist.entity.Playlist;
import com.zerobase.plistbackend.module.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  private Long channelId;

  @Column(length = 50, nullable = false)
  private String channelName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @CreatedDate
  @Column(nullable = false)
  private Timestamp channelCreatedAt;

  private Timestamp channelFinishedAt;

//  @Column(nullable = false)
//  private Long channelCapacity;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private ChannelStatus channelStatus;

  @Column(nullable = false)
  private String channelHost;

  private int channelLastParticipantCount;

  @OneToOne(mappedBy = "channel", cascade = CascadeType.ALL)
  @Builder.Default
  private Playlist channelPlaylist = new Playlist();

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Participant> channelParticipants = new ArrayList<>();

  public static Channel createChannel(ChannelRequest request, User user, Category category) {
    Channel channel = Channel.builder()
        .channelName(request.getChannelName())
        .category(category)
        .channelStatus(ChannelStatus.CHANNEL_STATUS_ACTIVE)
        .channelHost(user.getUserName())
        .build();

    Participant participant = Participant.host(user, channel);
    channel.getChannelParticipants().add(participant);
    channel.getChannelPlaylist().connectChannel(channel);

    return channel;
  }

  public static void closeChannel(Channel channel, List<Participant> participantList) {
    Date date = new Date();
    channel.channelFinishedAt = new Timestamp(date.getTime());
    channel.channelStatus = ChannelStatus.CHANNEL_STATUS_CLOSED;
    channel.channelLastParticipantCount = channel.getChannelParticipants().size();
    for (Participant participant : participantList) {
      channel.removeParticipant(participant);
    }
  }

  public void removeParticipant(Participant participant) {
    channelParticipants.remove(participant);
    participant.setChannel(null);
    participant.setUser(null);
    participant.getUser().setParticipant(null);
  }
}