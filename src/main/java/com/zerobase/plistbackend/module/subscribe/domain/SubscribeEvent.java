package com.zerobase.plistbackend.module.subscribe.domain;

import com.zerobase.plistbackend.module.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscribeEvent {

  private User user;

  private String followerName;
}
