package com.zerobase.plistbackend.module.message.entity;

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
import java.sql.Timestamp;
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
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long messageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  private String messageContent;

  private String messageLink;

  private boolean readCheck;

  @CreatedDate
  @Column(updatable = false)
  private Timestamp messageCreatedAt;

  public static Message from(User user, String messageContent, String messageLink) {
    return Message.builder()
        .user(user)
        .messageContent(messageContent)
        .readCheck(false)
        .messageLink(messageLink)
        .build();
  }

  public void read() {
    this.readCheck = true;
  }
}
