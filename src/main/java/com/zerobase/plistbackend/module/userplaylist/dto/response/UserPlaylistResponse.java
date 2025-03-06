package com.zerobase.plistbackend.module.userplaylist.dto.response;

import com.zerobase.plistbackend.module.channel.util.ChannelResponseMapper;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPlaylistResponse {

  private Long userPlaylistId;
  private String userPlaylistName;
  private String userPlaylistThumbnail;
  private int videoCount;

  public static UserPlaylistResponse fromEntity(UserPlaylist userPlaylist) {

    return UserPlaylistResponse.builder()
        .userPlaylistId(userPlaylist.getUserPlaylistId())
        .userPlaylistName(userPlaylist.getUserPlaylistName())
        .userPlaylistThumbnail(ChannelResponseMapper.findThumbnail(userPlaylist.getVideoList()))
        .videoCount(userPlaylist.getVideoList().size())
        .build();
  }
}
