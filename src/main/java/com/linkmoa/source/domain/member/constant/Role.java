package com.linkmoa.source.domain.member.constant;

public enum Role {
	USER, ADMIN;

	public String getRoleName() {
		return "ROLE_" + name();
	}
}
