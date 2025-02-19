package com.zerobase.plistbackend.module.user.entity;

import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.user.dto.response.OAuth2Response;
import com.zerobase.plistbackend.module.user.type.UserRole;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(name = "user_email")
  private String userEmail;

  @Column(name = "user_name", length = 30, nullable = false)
  private String userName;

  @CreatedDate
  @Column(name = "user_created_at", updatable = false)
  private Timestamp userCreatedAt;

  @LastModifiedDate
  @Column(name = "user_updated_at")
  private Timestamp userUpdatedAt;

  @Column(name = "user_role")
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  @Column(name = "user_image")
  private String userImage;

  @Setter
  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private Participant participant;

  @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
  private List<UserPlaylist> playlists;

  public static User from(OAuth2Response response, UserRole userRole) {
    return User.builder()
        .userEmail(response.findEmail())
        .userName(response.findName())
        .userImage(response.findImage())
        .userRole(userRole).build();
  }

  public void updateRole(UserRole role) {
    this.userRole = role;
  }

  public Boolean existFavoritePlayList(List<UserPlaylist> playlists) {
    if(playlists == null) {
      return false;
    }
    for (UserPlaylist userPlaylist : playlists){
      if (userPlaylist.getUserPlaylistName().equals("favorite")) {
        return true;
      }
    }
    return false;
  }

  public void updateUserName(String name) {
    this.userName = name;
  }

  public void updateImage(String fileName) {
    this.userImage = fileName;
  }
}