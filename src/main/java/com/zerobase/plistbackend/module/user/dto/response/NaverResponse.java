package com.zerobase.plistbackend.module.user.dto.response;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String findProvider() {
        return "naver";
    }

    @Override
    public String findProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String findEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String findName() {
        return attribute.get("name").toString();
    }


    @Override
    public String findImage() {
        return attribute.get("profile_image").toString();
    }
}
