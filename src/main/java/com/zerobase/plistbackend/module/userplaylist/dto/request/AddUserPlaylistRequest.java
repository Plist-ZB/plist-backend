package com.zerobase.plistbackend.module.userplaylist.dto.request;

import lombok.Getter;

@Getter
public class AddUserPlaylistRequest {
  private String userPlaylistName;
  private String videoId;

}
