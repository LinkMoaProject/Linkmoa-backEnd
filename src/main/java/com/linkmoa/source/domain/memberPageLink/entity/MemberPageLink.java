package com.linkmoa.source.domain.memberPageLink.entity;


import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.global.entity.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="member_page_link")
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class MemberPageLink extends BaseEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_page_link_id")
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="member_id")
    private Member member;


    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="page_id")
    private Page page;

    @Enumerated(EnumType.STRING)
    @Column(name="permission_type",nullable = false)
    private PermissionType permissionType;



    public void setMember(Member member){
        this.member=member;
        member.getMemberPageLinks().add(this);
    }

    public void setPage(Page page){
        this.page=page;
        page.getMemberPageLinks().add(this);
    }


}
