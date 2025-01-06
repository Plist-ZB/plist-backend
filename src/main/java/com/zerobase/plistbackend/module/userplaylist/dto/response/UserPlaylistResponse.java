package com.zerobase.plistbackend.module.userplaylist.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.plistbackend.module.user.dto.UserDto;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import com.zerobase.plistbackend.module.userplaylist.model.Video;
import java.util.List;
import lombok.Builder;

@Builder
public class UserPlaylistResponse {

  @JsonProperty("userPlaylistId")
  private Long userPlaylistId;
  @JsonProperty("userDto")
  private UserDto userDto;
  @JsonProperty("userPlaylistName")
  private String userPlaylistName;
  @JsonProperty("videoList")
  private List<Video> videoList;

  public static UserPlaylistResponse from(UserPlaylist userPlaylist) {
    return UserPlaylistResponse.builder()
        .userPlaylistId(userPlaylist.getUserPlaylistId())
        .userDto(UserDto.from(userPlaylist.getUser()))
        .userPlaylistName(userPlaylist.getUserPlaylistName())
        .videoList(userPlaylist.getVideoList())
        .build();
  }
}
