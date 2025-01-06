package com.zerobase.plistbackend.module.channel.dto.request;

import com.zerobase.plistbackend.module.playlist.entity.Playlist;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelRequest {

  @Range(min=1, max = 50)
  private String channelName;
  private Long userPlaylistId;
  private String channelCategory;
  private String channelThumbnail;
  private Long channelCapacity;

}
