package com.linkmoa.source.domain.directory.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.linkmoa.source.domain.site.entity.Site;
import com.linkmoa.source.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "directory")
public class Directory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "directory_id")
	private Long id;

	@Column(name = "name", length = 80)
	private String directoryName;

	@Column(name = "description", length = 300)
	private String directoryDescription;

	@ManyToOne(
		fetch = FetchType.LAZY
	)
	@JoinColumn(
		name = "parent_directory_id"
	)
	private Directory parentDirectory;

	@OneToMany(
		mappedBy = "parentDirectory",
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<Directory> childDirectories = new ArrayList<>();

	@OneToMany(
		mappedBy = "directory",
		cascade = CascadeType.ALL
	)
	private List<Site> sites = new ArrayList<>();

	@Column(name = "order_index")
	private Integer orderIndex;

	@Builder
	public Directory(String directoryName, String directoryDescription, Integer orderIndex) {
		this.directoryName = directoryName;
		this.directoryDescription = directoryDescription;
		this.orderIndex = orderIndex;
	}

	public void setParentDirectory(Directory parentDirectory) {
		this.parentDirectory = parentDirectory;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	// 부모 디렉토리가 호출하는 함수
	public void addChildDirectory(Directory childDirectory) {
		childDirectories.add(childDirectory);
		childDirectory.setParentDirectory(this);
	}

	public void updateDirectoryNameAndDescription(String directoryName, String directoryDescription) {
		this.directoryName = directoryName;
		this.directoryDescription = directoryDescription;
	}

	public Directory cloneDirectory(Directory newParentDirectory) {

		// 현재 디렉토리 복제
		Directory clonedDirectory = Directory.builder()
			.directoryName(this.directoryName + " Copy")
			.directoryDescription(this.directoryDescription)
			.build();

		// 새로운 부모 디렉토리를 설정
		if (newParentDirectory != null) {
			newParentDirectory.addChildDirectory(clonedDirectory);
		}

		// 현재 디렉토리의 Site 복제
		for (Site site : this.sites) {
			Site clonedSite = Site.builder()
				.siteName(site.getSiteName())
				.siteUrl(site.getSiteUrl())
				.directory(clonedDirectory) // 복제된 디렉토리와 연관
				.build();
			clonedDirectory.getSites().add(clonedSite);
		}

		// 하위 디렉토리 재귀적으로 복제
		for (Directory childDirectory : this.childDirectories) {
			Directory clonedChildDirectory = childDirectory.cloneDirectory(clonedDirectory);
			clonedDirectory.getChildDirectories().add(clonedChildDirectory);
		}

		return clonedDirectory;
	}

	public Integer getNextOrderIndex() {
		return Stream.concat(
				this.getChildDirectories().stream().map(Directory::getOrderIndex),
				this.getSites().stream().map(Site::getOrderIndex)
			)
			.filter(order -> order != null)  // null 제거
			.max(Integer::compareTo)         // 최대값
			.orElse(0) + 1;            // 없으면 1부터 시작
	}
}
