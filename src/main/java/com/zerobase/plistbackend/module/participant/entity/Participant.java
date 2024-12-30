package com.zerobase.plistbackend.module.participant.entity;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
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
@Table(name = "participant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

  @Id
  @Column(name = "participant_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long participantId;

  @ManyToOne(fetch = FetchType.LAZY)
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

  public Boolean getHostStatus() {
    return isHost;
  }
}
