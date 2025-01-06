package com.zerobase.plistbackend.module.user.dto;

import com.zerobase.plistbackend.module.participant.dto.response.ParticipantResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.type.UserRole;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private Long userId;
  private String userEmail;
  private String userName;
  private Timestamp userCreatedAt;
  private Timestamp userUpdatedAt;
  private UserRole userRole;
  private String userImage;
  private List<ParticipantResponse> participants;

  public static UserDto from(User user) {
    List<ParticipantResponse> participantResponseList = user.getParticipants().stream()
        .map(ParticipantResponse::createParticipantResponse).toList();
    return UserDto.builder()
        .userId(user.getUserId())
        .userEmail(user.getUserEmail())
        .userName(user.getUserName())
        .userCreatedAt(user.getUserCreatedAt())
        .userUpdatedAt(user.getUserUpdatedAt())
        .userRole(user.getUserRole())
        .userImage(user.getUserImage())
        .participants(participantResponseList)
        .build();
  }
}
