package com.zerobase.plistbackend.module.fcm.entity;

import com.zerobase.plistbackend.module.user.entity.User;
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

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "fcm_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FCMToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long fcmTokenId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  private String fcmTokenValue;

  private Timestamp fcmTokenCreatedAt;

  public static FCMToken from(String token, User user) {
    return FCMToken.builder()
        .user(user)
        .fcmTokenValue(token)
        .fcmTokenCreatedAt(new Timestamp(System.currentTimeMillis()))
        .build();
  }

  public void updateFCMToken() {
    this.fcmTokenCreatedAt = new Timestamp(System.currentTimeMillis());
  }
}