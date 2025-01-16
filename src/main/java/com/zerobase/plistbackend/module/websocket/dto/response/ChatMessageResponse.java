package com.zerobase.plistbackend.module.websocket.dto.response;

import com.zerobase.plistbackend.module.websocket.dto.request.ChatMessageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
  private String sender;
  private String message;
  private String userProfileImg;

  public static ChatMessageResponse from(ChatMessageRequest request, String userProfileImg) {
    return ChatMessageResponse.builder()
        .sender(request.getSender())
        .message(request.getMessage())
        .userProfileImg(userProfileImg)
        .build();
  }
}
