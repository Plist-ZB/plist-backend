package com.zerobase.plistbackend.module.chatting.dto.response;

import com.zerobase.plistbackend.module.chatting.dto.request.ChatMessageRequest;
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
  private String thumbnail;

  public static ChatMessageResponse from(ChatMessageRequest request) {
    return ChatMessageResponse.builder()
        .sender(request.getSender())
        .message(request.getMessage())
        .thumbnail(request.getThumbnail())
        .build();
  }
}
