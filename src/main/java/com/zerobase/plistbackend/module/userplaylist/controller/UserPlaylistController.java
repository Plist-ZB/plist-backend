package com.zerobase.plistbackend.module.userplaylist.controller;

import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.userplaylist.dto.request.CreateUserPlaylistRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.response.CreateUserPlaylistResponse;
import com.zerobase.plistbackend.module.userplaylist.service.UserPlaylistService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserPlaylistController {

  private final UserPlaylistService userPlaylistService;

  @PostMapping("/create-userplaylist-title")
  public ResponseEntity<?> createUserPlayList(@AuthenticationPrincipal CustomOAuth2User user,
      @RequestBody CreateUserPlaylistRequest userPlaylistName) throws IOException {
    userPlaylistService.createUserPlayListName(user, userPlaylistName);
    return ResponseEntity.ok(
        new CreateUserPlaylistResponse(userPlaylistName.getUserPlaylistName())
    );
  }

}
