package com.linkmoa.source.auth.oauth2.principal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.linkmoa.source.domain.member.entity.Member;

import lombok.Getter;

@Getter
public class PrincipalDetails implements OAuth2User, UserDetails {

	private Member member;
	private Map<String, Object> attributes; // attributes : 구글에서 받아온 정보들

	public PrincipalDetails() {

	}

	/**
	 * 자체 로그인
	 */
	public PrincipalDetails(Member member) {
		this.member = member;
	}

	/**
	 * OAuth2 로그인
	 */
	public PrincipalDetails(Member member, Map<String, Object> attributes) {
		this.member = member;
		this.attributes = attributes;
	}

	/**
	 * 자체 로그인, OAuth2 로그인 공통
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return String.valueOf(member.getRole());
			}
		});

		return authorities;
	}

	// UserDetails //
	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		if (member.getEmail() == null) {
			return new IllegalStateException("member.getEmail() is null").toString();
		}
		return member.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	// OAuth2User //
	@Override
	public String getName() {
		return member.getNickname();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	public String getEmail() {
		return member.getEmail();
	}

	public Long getId() {
		return member.getId();
	}
}
