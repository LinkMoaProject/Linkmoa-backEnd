package com.linkmoa.source.auth.oauth2.entity;

public interface OAuth2UserInfo {
	String getProvider();

	String getProviderId();

	String getEmail();

	String getName();
}
