package com.zerobase.plistbackend.module.home.service;

import com.zerobase.plistbackend.module.home.dto.response.VideoResponse;
import java.util.List;

public interface HomeService {

  List<VideoResponse> searchVideo(String keyword);
}
