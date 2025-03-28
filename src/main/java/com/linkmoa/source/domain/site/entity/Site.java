package com.linkmoa.source.domain.site.entity;

import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "site")
public class Site extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "site_id")
	private Long id;

	@Column(name = "name", length = 45)
	private String siteName;

	@Column(name = "url", length = 255)
	private String siteUrl;

	@ManyToOne(
		fetch = FetchType.LAZY
	)
	@JoinColumn(name = "directory_id")
	private Directory directory;

	@Column(name = "order_index")
	private Integer orderIndex;

	@Builder
	public Site(String siteName, String siteUrl, Directory directory, Integer orderIndex) {
		this.siteName = siteName;
		this.siteUrl = siteUrl;
		this.orderIndex = orderIndex;
		setDirectory(directory);

	}

	public void setDirectory(Directory directory) {

		if (this.directory != null && this.directory != directory) {
			// 기존 디렉토리에서 먼저 제거
			this.directory.getSites().remove(this);
		}

		// 새로운 디렉토리에 추가
		this.directory = directory;
		if (directory != null && !directory.getSites().contains(this)) {
			directory.getSites().add(this);
		}

	}

	public void updateSiteNameAndUrl(String siteName, String siteUrl) {
		this.siteName = siteName;
		this.siteUrl = siteUrl;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

}
