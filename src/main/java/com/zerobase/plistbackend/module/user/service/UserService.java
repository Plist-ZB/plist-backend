package com.zerobase.plistbackend.module.user.service;

import com.zerobase.plistbackend.module.user.dto.request.UserProfileRequest;
import com.zerobase.plistbackend.module.user.dto.response.HostPlaytimeResponse;
import com.zerobase.plistbackend.module.user.dto.response.OtherProfileResponse;
import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;


public interface UserService {

  ProfileResponse findProfile(String accessToken);

  OtherProfileResponse findUserIdProfile(Long userId);

  void withdrawUser(Long userId);

  ProfileResponse editProfile(UserProfileRequest request, Long userId);

  HostPlaytimeResponse getHistoryOfHost(Long hostId, int year);
}
