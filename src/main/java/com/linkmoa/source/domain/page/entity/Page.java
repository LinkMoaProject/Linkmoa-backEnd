package com.linkmoa.source.domain.page.entity;

import java.util.ArrayList;
import java.util.List;

import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "page")
public class Page extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "page_id")
	private Long id;

	@Column(name = "title", length = 50)
	private String pageTitle;

	@Column(name = "description", length = 300)
	private String pageDescription;

	@Enumerated(EnumType.STRING)
	@Column(name = "page_type", nullable = false)
	private PageType pageType;
	@OneToMany(
		mappedBy = "page",
		cascade = CascadeType.ALL,  // Page 삭제 시 관련된 MemberPageLink도 삭제
		orphanRemoval = true
	)
	private List<MemberPageLink> memberPageLinks = new ArrayList<>();

	@OneToOne(
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	@JoinColumn(name = "root_directory_id", unique = true) // rootDirectory와 1:1 매핑
	private Directory rootDirectory;

	@Builder
	public Page(String pageTitle, String pageDescription, PageType pageType, Directory rootDirectory) {
		this.pageTitle = pageTitle;
		this.pageDescription = pageDescription;
		this.pageType = pageType;
		this.rootDirectory = rootDirectory;
	}

}
