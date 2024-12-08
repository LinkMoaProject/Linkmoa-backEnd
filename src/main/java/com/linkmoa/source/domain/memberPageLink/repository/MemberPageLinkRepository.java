package com.linkmoa.source.domain.memberPageLink.repository;

import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberPageLinkRepository extends JpaRepository<MemberPageLink,Long> {


    @Query("SELECT m.permissionType FROM member_page_link m where m.member.id =:memberId AND m.page.id =:pageId")
    PermissionType findPermissionTypeByMemberIdAndPageId(@Param("memberId")Long memberId,@Param("pageId") Long pageId);

    @Query("DELETE FROM member_page_link m WHERE m.member.id = :memberId AND m.page.id = :pageId")
    void deleteByMemberIdAndPageId(@Param("memberId") Long memberId, @Param("pageId") Long pageId);

    @Query("SELECT m FROM member_page_link m WHERE m.member.id = :memberId AND m.page.id = :pageId")
    Optional<MemberPageLink> findByMemberAndPage(@Param("memberId") Long memberId, @Param("pageId") Long pageId);


    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END " +
            "FROM member_page_link m WHERE m.member.id = :memberId AND m.page.id = :pageId")
    boolean existsByMemberAndPage(@Param("memberId") Long memberId, @Param("pageId") Long pageId);



}
