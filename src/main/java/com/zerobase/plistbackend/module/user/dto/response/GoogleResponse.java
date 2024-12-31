package com.zerobase.plistbackend.module.user.dto.response;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {

  private final Map<String, Object> attribute;

  public GoogleResponse(Map<String, Object> attribute) {
    this.attribute = attribute;
  }

  @Override
  public String findProvider() {
    return "google";
  }

  @Override
  public String findProviderId() {
    if (attribute.containsKey("sub")) {
      return attribute.get("sub").toString();
    }
    return null;
  }

  @Override
  public String findEmail() {
    if (attribute.containsKey("email")) {
      return attribute.get("email").toString();
    }
    return null;
  }

  @Override
  public String findName() {
    if (attribute.containsKey("name")) {
      return attribute.get("name").toString();
    }
    return null;
  }

  @Override
  public String findImage() {
    if (attribute.containsKey("picture")) {
      return attribute.get("picture").toString();
    }
    return null;
  }

}
