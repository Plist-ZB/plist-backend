package com.zerobase.plistbackend.module.subscribe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowerInfoResponse {

  private Long followerId;

  private String followerName;

  private String followerImage;
}