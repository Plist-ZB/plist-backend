package com.zerobase.plistbackend.module.user.entity;

import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.user.type.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(name = "user_email")
  private String userEmail;

  @Column(name = "user_name", nullable = false)
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

  @OneToMany(mappedBy = "user")
  private List<Participant> participants = new ArrayList<>();

}