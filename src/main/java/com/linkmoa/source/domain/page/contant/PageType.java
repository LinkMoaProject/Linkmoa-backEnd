package com.linkmoa.source.domain.page.contant;

public enum PageType {

	PERSONAL, // 개인
	BASIC, // 1~ 10명
	STANDARD, // 1~20명
	PREMIUM; // 1~30명

	@Override
	public String toString() {
		switch (this) {
			case PERSONAL:
				return "PERSONAL ";
			case BASIC:
				return "BASIC";
			case STANDARD:
				return "STANDARD";
			case PREMIUM:
				return "PREMIUM";
			default:
				return super.toString();
		}
	}
}
