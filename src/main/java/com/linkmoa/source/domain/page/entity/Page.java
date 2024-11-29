package com.linkmoa.source.domain.page.entity;


import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name="page")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page extends BaseEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="page_id")
    private Long id;

    @Column(name="page_title",length = 20)
    private String pageTitle;

    @Column(name="page_description",length = 100)
    private String pageDescription;

    @Enumerated(EnumType.STRING)
    private PageType pageType;

    @OneToMany(
            mappedBy = "page",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MemberPageLink> memberPageLinks = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "root_directory_id", unique = true) // rootDirectory와 1:1 매핑
    private Directory rootDirectory;

    @Builder
    public Page(String pageTitle,String pageDescription,PageType pageType,Directory rootDirectory){
        this.pageTitle=pageTitle;
        this.pageDescription=pageDescription;
        this.pageType=pageType;
        this.rootDirectory=rootDirectory;
    }




}
