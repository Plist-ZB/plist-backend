package com.zerobase.plistbackend.module.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayTimeResponse {
    private String totalPlayTime;
    private int totalParticipant;
    private Long totalFollowers;
}
