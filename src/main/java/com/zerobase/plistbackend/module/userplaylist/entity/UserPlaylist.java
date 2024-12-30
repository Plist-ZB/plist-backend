package com.zerobase.plistbackend.module.userplaylist.entity;

import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.userplaylist.model.Video;
import jakarta.persistence.Column;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "userplaylist")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPlaylist {

  @Id
  @Column(name = "userplaylist_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userPlaylistId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "userplaylist_name")
  private String userPlaylistName;

  @Lob
  @Column(name = "video")
  private Video video;
}
