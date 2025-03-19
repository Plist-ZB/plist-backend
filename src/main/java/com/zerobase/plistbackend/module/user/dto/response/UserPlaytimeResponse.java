package com.zerobase.plistbackend.module.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPlaytimeResponse {
    private String totalPlayTime;

    @Builder
    private UserPlaytimeResponse(String totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }
    public static UserPlaytimeResponse from(String totalPlayTime) {
        return new UserPlaytimeResponse(totalPlayTime);
    }
}
