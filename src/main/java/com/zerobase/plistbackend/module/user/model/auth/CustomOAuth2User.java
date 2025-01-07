package com.zerobase.plistbackend.module.user.model.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

  private final UserDetail userDetail;

  @Override
  public Map<String, Object> getAttributes() {
    return null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> collection = new ArrayList<GrantedAuthority>();
    collection.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return userDetail.getRole();
      }
    });
    return collection;
  }

  @Override
  public String getName() {
    return userDetail.getName();
  }

  public String findEmail() {
    return userDetail.getEmail();
  }

  public Boolean findIsMember() {
    return userDetail.getIsMember();
  }

  public Long findId() {
    return userDetail.getId();
  }

}
