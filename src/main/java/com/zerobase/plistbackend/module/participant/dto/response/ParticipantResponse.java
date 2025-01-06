package com.zerobase.plistbackend.module.participant.dto.response;

import com.zerobase.plistbackend.module.participant.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponse {

  private Long participantId;
  private Long userId;
  private Long channelId;
  private boolean isHost;

  public static ParticipantResponse createParticipantResponse(Participant participant) {
    return ParticipantResponse.builder()
        .participantId(participant.getParticipantId())
        .userId(participant.getUser().getUserId())
        .channelId(participant.getChannel().getChannelId())
        .isHost(participant.getIsHost())
        .build();
  }
}