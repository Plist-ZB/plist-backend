package com.zerobase.plistbackend.module.user.service;

import com.zerobase.plistbackend.module.user.dto.response.ProfileResponse;

public interface UserService {

   ProfileResponse findProfile(String accessToken);

}
