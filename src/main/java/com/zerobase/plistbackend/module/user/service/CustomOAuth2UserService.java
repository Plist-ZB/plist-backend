package com.zerobase.plistbackend.module.user.service;

import com.zerobase.plistbackend.module.user.dto.response.OAuth2Response;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.model.auth.UserDetail;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.user.type.RegistrationId;
import com.zerobase.plistbackend.module.user.type.UserRole;
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

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);
    OAuth2Response oAuth2Response = RegistrationId.fromId(userRequest.getClientRegistration().getRegistrationId(), oAuth2User);

    String email = oAuth2Response.findEmail();
    log.info("Request Login email: {}", email);
    User existData = userRepository.findByUserEmail(email);

    if(existData == null) {
      existData = User.builder()
          .userEmail(email)
          .userName(oAuth2Response.findName())
          .userImage(oAuth2Response.findImage())
          .userRole(UserRole.ROLE_USER).build();

      userRepository.save(existData);
    }

    UserDetail userDetail = UserDetail.builder()
        .name(existData.getUserName())
        .email(existData.getUserEmail())
        .role(String.valueOf(existData.getUserRole())).build();

    return new CustomOAuth2User(userDetail);
  }
}
