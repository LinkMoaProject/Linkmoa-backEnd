package com.linkmoa.source.domain.page.contant;

public enum PageType {
	/**
	 * 개인 페이지
	 */
	PERSONAL(1, 1),

	/**
	 * 공유 페이지
	 */
	BASIC(1, 10),
	STANDARD(1, 20),
	PREMIUM(1, 30);

	private final int minUsers;
	private final int maxUsers;

	PageType(int minUsers, int maxUsers) {
		this.minUsers = minUsers;
		this.maxUsers = maxUsers;
	}

	public int getMinUsers() {
		return minUsers;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

	/**
	 * 지정된 사용자 수가 이 페이지 유형의 허용 범위에 포함되는지 확인함
	 * @param userCount 검사할 사용자 수
	 * @return 사용자 수가 해당 페이지 범위 내에 있으면 true 아니면 false
	 */
	public boolean isValidUserCount(int userCount) {
		return userCount >= minUsers && userCount <= maxUsers;
	}

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
