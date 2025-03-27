package com.zerobase.plistbackend.module.subscribe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FolloweeInfoResponse {

  private Long followeeId;

  private String followeeName;
}