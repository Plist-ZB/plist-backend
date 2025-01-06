package com.zerobase.plistbackend.module.userplaylist.model;

import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Video {

  private Long id;
  private String videoName;
  private String videoThumbnail;
  private String videoId;
  private String videoDescription;

  public static Video createVideo (VideoRequest videoRequest, List<Video> videoList) {

    Long increased_id = init(videoList);

    return Video.builder()
        .id(increased_id)
        .videoName(videoRequest.getVideoName())
        .videoThumbnail(videoRequest.getVideoThumbnail())
        .videoId(videoRequest.getVideoId())
        .videoDescription(videoRequest.getVideoDescription())
        .build();
  }

  private static Long init(List<Video> videoList) {
    long id = 1L;
    if (videoList.isEmpty()) {
      return id;
    } else {
      Video video = videoList.get(videoList.size() - 1);
      id = video.getId() + 1;
      return id;
    }
  }
}
