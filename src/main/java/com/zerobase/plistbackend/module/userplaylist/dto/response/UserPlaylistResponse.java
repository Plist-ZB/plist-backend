package com.zerobase.plistbackend.module.userplaylist.dto.response;

import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import java.util.Optional;
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
    String thumbnail = "";
    if (!userPlaylist.getVideoList().isEmpty()) {
      thumbnail = userPlaylist.getVideoList().get(0).getVideoThumbnail();
    }

    return UserPlaylistResponse.builder()
        .userPlaylistId(userPlaylist.getUserPlaylistId())
        .userPlaylistName(userPlaylist.getUserPlaylistName())
        .userPlaylistThumbnail(thumbnail)
        .videoCount(userPlaylist.getVideoList().size())
        .build();
  }
}
