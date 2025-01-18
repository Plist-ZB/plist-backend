package com.zerobase.plistbackend.module.websocket.dto.response;

import com.zerobase.plistbackend.module.user.entity.User;
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

  public static ChatMessageResponse from(ChatMessageRequest request,User findUser) {
    return ChatMessageResponse.builder()
        .sender(findUser.getUserName())
        .message(request.getMessage())
        .userProfileImg(findUser.getUserImage())
        .build();
  }
}
