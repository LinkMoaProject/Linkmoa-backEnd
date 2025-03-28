package com.linkmoa.source.domain.page.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkmoa.source.domain.page.entity.Page;

public interface PageRepository extends JpaRepository<Page, Long>, PageRepositoryCustom {

	// TODO : 검색 관련 모든 기능 수정해야됨
	@Query(value =
		"WITH RECURSIVE DirectoryTree AS ( " +
			"   SELECT directory_id, parent_directory_id, name " +
			"   FROM directory " +
			"   WHERE directory_id = :rootDirectoryId " +
			"   UNION ALL " +
			"   SELECT d.directory_id, d.parent_directory_id, d.name " +
			"   FROM directory d " +
			"   INNER JOIN DirectoryTree dt ON d.parent_directory_id = dt.directory_id " +
			") " +
			"SELECT " +
			"    d.directory_id AS directory_id, " +
			"    d.name AS directory_name, " +
			"    CASE WHEN f1.favorite_id IS NOT NULL THEN true ELSE false END AS is_directory_favorite, " +
			"    s.site_id AS site_id, " +
			"    s.name AS site_name, " +
			"    s.url AS site_url, " +
			"    CASE WHEN f2.favorite_id IS NOT NULL THEN true ELSE false END AS is_site_favorite " +
			"FROM DirectoryTree d " +
			"LEFT JOIN site s ON s.directory_id = d.directory_id " +
			"LEFT JOIN favorite f1 ON f1.item_id = d.directory_id AND f1.member_id = :memberId AND f1.item_type = 'DIRECTORY' "
			+
			"LEFT JOIN favorite f2 ON f2.item_id = s.site_id AND f2.member_id = :memberId AND f2.item_type = 'SITE' " +
			"WHERE d.name LIKE CONCAT('%', :name, '%') OR s.name LIKE CONCAT('%', :name, '%') " +
			"ORDER BY d.name ASC, s.name ASC", nativeQuery = true)
	List<Object[]> findDirectoriesAndSitesByNameKeyword(
		@Param("name") String name,
		@Param("rootDirectoryId") Long rootDirectoryId,
		@Param("memberId") Long memberId);

}
