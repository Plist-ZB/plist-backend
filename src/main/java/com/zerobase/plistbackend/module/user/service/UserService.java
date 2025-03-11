package com.zerobase.plistbackend.module.user.service;

import com.zerobase.plistbackend.module.user.dto.request.UserProfileRequest;
import com.zerobase.plistbackend.module.user.dto.response.PlayTimeResponse;
import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;


public interface UserService {

   ProfileResponse findProfile(String accessToken);

   void withdrawUser(Long userId);

   ProfileResponse editProfile(UserProfileRequest request, Long userId);

   PlayTimeResponse getPlayTimeForHistroyOfHost(Long hostId, int year);
}
