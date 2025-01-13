package com.zerobase.plistbackend.module.userplaylist.dto.response;

import com.zerobase.plistbackend.module.home.model.Video;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailUserPlaylistResponse {

  private Long userPlaylistId;
  private String userPlaylistName;
  private List<Video> videoList;

  public static DetailUserPlaylistResponse fromEntity(UserPlaylist userPlaylist) {

    return DetailUserPlaylistResponse.builder()
        .userPlaylistId(userPlaylist.getUserPlaylistId())
        .userPlaylistName(userPlaylist.getUserPlaylistName())
        .videoList(userPlaylist.getVideoList())
        .build();
  }
}
