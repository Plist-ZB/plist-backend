package com.zerobase.plistbackend.module.user.dto.response;

import java.util.LinkedHashMap;
import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String findProvider() {
        return "kakao";
    }

    @Override
    public String findProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String findEmail() {
        Object kakao_account = attribute.get("kakao_account");
        LinkedHashMap accountMap = (LinkedHashMap) kakao_account;
        return accountMap.get("email").toString();
    }

    @Override
    public String findName() {
        Object kakao_account = attribute.get("kakao_account");
        LinkedHashMap accountMap = (LinkedHashMap) kakao_account;
        Object profile = accountMap.get("profile");
        LinkedHashMap profileMap = (LinkedHashMap) profile;
        return profileMap.get("nickname").toString();
    }


    @Override
    public String findImage() {
        Object kakao_account = attribute.get("kakao_account");
        LinkedHashMap accountMap = (LinkedHashMap) kakao_account;
        Object profile = accountMap.get("profile");
        LinkedHashMap profileMap = (LinkedHashMap) profile;
        return profileMap.get("thumbnail_image_url").toString();
    }
}
