package com.zerobase.plistbackend.module.playlist.domain;

import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaylistCrudEvent {
  private Long channelId;
  private CustomOAuth2User customOAuth2User;
}
