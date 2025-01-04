package com.zerobase.plistbackend.module.userplaylist.service;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.CreateUserPlaylistRequest;
import java.io.IOException;

public interface UserPlaylistService {

  void createUserPlayListName(CustomOAuth2User user, CreateUserPlaylistRequest userPlaylistName) throws IOException;

}
