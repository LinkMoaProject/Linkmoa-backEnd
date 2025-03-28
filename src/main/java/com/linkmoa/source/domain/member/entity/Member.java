package com.linkmoa.source.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

import com.linkmoa.source.domain.member.constant.Gender;
import com.linkmoa.source.domain.member.constant.Role;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "email")
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@Column(name = "nickname")
	private String nickname;

	// provider : google이 들어감
	@Column(name = "provider")
	private String provider;

	// providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
	@Column(name = "provider_id")
	private String providerId;

	@Column(name = "age_range")
	private String ageRange;

	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name = "job")
	private String job;

	@OneToMany(
		mappedBy = "member",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<MemberPageLink> memberPageLinks = new ArrayList<>();

	@Builder
	public Member(String email, Role role, String provider, String providerId) {
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
	}

	public void updateMember(Member member) {
		if (member.getEmail() != null) {
			this.email = member.getEmail();
		}

		if (member.getNickname() != null) {
			this.nickname = member.getNickname();
		}

		if (member.getRole() != null) {
			this.role = member.getRole();
		}
	}

	public void updateSignUpMember(String ageRange, Gender gender, String job, String nickname) {
		this.ageRange = ageRange;
		this.gender = gender;
		this.job = job;
		this.nickname = nickname;
	}
}
