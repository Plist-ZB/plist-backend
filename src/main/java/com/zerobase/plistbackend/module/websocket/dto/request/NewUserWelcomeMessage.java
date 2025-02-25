package com.zerobase.plistbackend.module.websocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserWelcomeMessage {
    private Long channelId;
    private String message;

}
