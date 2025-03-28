package com.linkmoa.source.domain.memberPageLink.entity;

import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_page_link")
public class MemberPageLink extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_page_link_id")
	private Long id;

	@ManyToOne(
		fetch = FetchType.LAZY
	)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(
		fetch = FetchType.LAZY
	)
	@JoinColumn(name = "page_id")
	private Page page;

	@Enumerated(EnumType.STRING)
	@Column(name = "permission_type", nullable = false)
	private PermissionType permissionType;

	@Builder
	public MemberPageLink(Member member, Page page, PermissionType permissionType) {
		this.permissionType = permissionType;
		setMember(member);
		setPage(page);

	}

	private void setMember(Member member) {
		this.member = member;
		member.getMemberPageLinks().add(this);
	}

	private void setPage(Page page) {
		this.page = page;
		page.getMemberPageLinks().add(this);
	}

}
