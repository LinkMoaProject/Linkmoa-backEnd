package com.linkmoa.source.auth.jwt.refresh.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor
@Getter
@RedisHash(value = "RefreshToken", timeToLive = 86400) // TTL: 24시간
public class RefreshToken {

	@Id
	private String refreshToken;

	private String memberEmail;

	@Builder
	public RefreshToken(String refreshToken, String memberEmail) {
		this.refreshToken = refreshToken;
		this.memberEmail = memberEmail;
	}
}
