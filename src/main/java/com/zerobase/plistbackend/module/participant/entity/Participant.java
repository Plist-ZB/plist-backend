package com.zerobase.plistbackend.module.participant.entity;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "participant")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

  @Id
  @Column(name = "participant_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long participantId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @Column(name = "is_host", nullable = false)
  private Boolean isHost;

  @CreatedDate
  @Column(name = "participant_created_at")
  private Timestamp participantCreatedAt;

  public static Participant host(User user, Channel channel) {
    return Participant.builder()
        .user(user)
        .channel(channel)
        .isHost(true)
        .build();
  }

  public static Participant viewer(User user, Channel channel) {
    return Participant.builder()
        .user(user)
        .channel(channel)
        .isHost(false)
        .build();
  }

  public void disconnectChannel() {
    this.channel = null;
    this.user.disconnectParticipant();
  }
}
