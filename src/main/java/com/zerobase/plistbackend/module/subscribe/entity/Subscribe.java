package com.zerobase.plistbackend.module.subscribe.entity;

import com.zerobase.plistbackend.module.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscribe {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long subscribeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "followee_id")
  private User followee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "follower_id")
  private User follower;

  public static Subscribe from(User followee, User follower) {
    return Subscribe.builder()
        .followee(followee)
        .follower(follower)
        .build();
  }
}