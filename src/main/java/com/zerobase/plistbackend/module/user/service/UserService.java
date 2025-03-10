package com.zerobase.plistbackend.module.user.service;

import com.zerobase.plistbackend.module.user.dto.request.UserProfileRequest;
import com.zerobase.plistbackend.module.user.dto.response.PlayTimeResponse;
import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;

import java.util.List;

public interface UserService {

   ProfileResponse findProfile(String accessToken);

   void withdrawUser(Long userId);

   ProfileResponse editProfile(UserProfileRequest request, Long userId);

   List<PlayTimeResponse> getPlaytime(Long hostId, int year);
}
