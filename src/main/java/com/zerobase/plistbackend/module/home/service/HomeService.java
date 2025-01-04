package com.zerobase.plistbackend.module.home.service;

import com.zerobase.plistbackend.module.home.dto.response.VideoResponse;
import com.zerobase.plistbackend.module.userplaylist.domain.Video;
import java.io.IOException;
import java.util.List;

public interface HomeService {

  List<VideoResponse> searchVideo(String searchValue);

  Video getVideo(String videoId) throws IOException;
}
