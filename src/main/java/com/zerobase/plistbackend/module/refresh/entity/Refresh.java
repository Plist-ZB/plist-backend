package com.zerobase.plistbackend.module.refresh.entity;

import com.zerobase.plistbackend.module.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "refresh")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refresh {

  @Id
  @Column(name = "refresh_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long refreshId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "refresh_token")
  private String refreshToken;

  @Column(name = "refresh_expiration")
  private Timestamp refreshExpiration;
}
