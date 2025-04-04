package com.linkmoa.source.domain.memberPageLink.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;

public interface MemberPageLinkRepository extends JpaRepository<MemberPageLink, Long>, MemberPageLinkRepositoryCustom {

	@Query("SELECT m.permissionType FROM MemberPageLink m where m.member.id =:memberId AND m.page.id =:pageId")
	PermissionType findPermissionTypeByMemberIdAndPageId(@Param("memberId") Long memberId,
		@Param("pageId") Long pageId);

	@Query("DELETE FROM MemberPageLink m WHERE m.member.id = :memberId AND m.page.id = :pageId")
	@Modifying
	void deleteByMemberIdAndPageId(@Param("memberId") Long memberId, @Param("pageId") Long pageId);

	@Query("SELECT m FROM MemberPageLink m WHERE m.member.id = :memberId AND m.page.id = :pageId")
	Optional<MemberPageLink> findByMemberAndPage(@Param("memberId") Long memberId, @Param("pageId") Long pageId);

	@Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END " +
		"FROM MemberPageLink m WHERE m.member.id = :memberId AND m.page.id = :pageId")
	boolean existsByMemberAndPage(@Param("memberId") Long memberId, @Param("pageId") Long pageId);

	@Query("SELECT COUNT(mpl) FROM MemberPageLink mpl WHERE mpl.page.id = :pageId")
	long countMembersInSharedPage(@Param("pageId") Long pageId);

	@Query("SELECT COUNT(mpl) FROM MemberPageLink mpl WHERE mpl.page.id = :pageId AND mpl.permissionType = com.linkmoa.source.domain.memberPageLink.constant.PermissionType.HOST AND mpl.member = :member")
	long countHostMembersInSharedPage(@Param("pageId") Long pageId, @Param("member") Member member);

	void deleteByMemberId(Long memberId);

	@Query("SELECT m FROM MemberPageLink m " +
		"WHERE m.member.id = :memberId " +
		"AND m.permissionType = com.linkmoa.source.domain.memberPageLink.constant.PermissionType.HOST " +
		"AND (SELECT COUNT(mp) FROM MemberPageLink mp " +
		"      WHERE mp.page.id = m.page.id " +
		"      AND mp.permissionType = com.linkmoa.source.domain.memberPageLink.constant.PermissionType.HOST) = 1")
	List<MemberPageLink> findUniqueHostByMemberId(@Param("memberId") Long memberId);

}
