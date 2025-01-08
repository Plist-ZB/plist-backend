package com.zerobase.plistbackend.module.chatting.dto.response;

import com.zerobase.plistbackend.module.chatting.dto.request.ChatMessageRequest;
import com.zerobase.plistbackend.module.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
  private String sender;
  private String message;
  private String userProfileImg;

  public static ChatMessageResponse from(ChatMessageRequest request, User findUser) {
    return ChatMessageResponse.builder()
        .sender(request.getSender())
        .message(request.getMessage())
        .userProfileImg(findUser.getUserImage())
        .build();
  }
}
