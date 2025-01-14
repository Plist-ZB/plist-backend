package com.zerobase.plistbackend.module.user.service;

import com.zerobase.plistbackend.module.user.dto.response.OAuth2Response;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.model.auth.UserDetail;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.RegistrationId;
import com.zerobase.plistbackend.module.user.type.UserRole;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import com.zerobase.plistbackend.module.userplaylist.repository.UserPlaylistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;
  private final UserPlaylistRepository userPlaylistRepository;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);
    OAuth2Response oAuth2Response = RegistrationId.fromId(
        userRequest.getClientRegistration().getRegistrationId(), oAuth2User);

    String email = oAuth2Response.findEmail();
    log.info("Request Login email: {}", email);
    User existData = userRepository.findByUserEmail(email);
    Boolean isMember = true;

    if (existData == null) {
      isMember = false;
      existData = User.from(oAuth2Response, UserRole.ROLE_USER);
      userRepository.save(existData);
    }

    if (existData.getUserRole().equals(UserRole.ROLE_NONE)) {
      isMember = false;
      existData.updateRole(UserRole.ROLE_USER);
    }

    if (!existData.existFavoritePlayList(existData.getPlaylists())) {
      UserPlaylist userPlaylist = UserPlaylist.createUserPlaylist(existData,
          new UserPlaylistRequest("favorite"));
      userPlaylistRepository.save(userPlaylist);
    }

    return new CustomOAuth2User(UserDetail.from(existData, isMember));
  }
}
