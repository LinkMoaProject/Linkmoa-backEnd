package com.init.setting.auth.oauth2.entity;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
