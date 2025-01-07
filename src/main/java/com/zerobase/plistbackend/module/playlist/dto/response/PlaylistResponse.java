package com.zerobase.plistbackend.module.playlist.dto.response;

import com.zerobase.plistbackend.module.playlist.entity.Playlist;
import com.zerobase.plistbackend.module.userplaylist.model.Video;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistResponse {

  private Long playlistId;
  private List<Video> videoList;

  public static PlaylistResponse from(Playlist playlist) {
    return PlaylistResponse.builder()
        .playlistId(playlist.getPlaylistId())
        .videoList(playlist.getVideoList())
        .build();
  }
}