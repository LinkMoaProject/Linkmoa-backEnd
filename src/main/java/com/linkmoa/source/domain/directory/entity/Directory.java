package com.linkmoa.source.domain.directory.entity;


import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.site.entity.Site;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name="directory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Directory {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="directory_id")
    private Long id;

    @Column(name="directory_name")
    private String directoryName;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name="parent_directory_id"
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
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Site> sites =new ArrayList<>();

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="member_id")
    private Member member;


    @Builder
    public Directory(String directoryName,Member member,Directory parentDirectory){
        this.directoryName=directoryName;
        this.parentDirectory=parentDirectory;
        setMember(member);
    }

    public void setMember(Member member){
        this.member=member;
        member.getDirectory().add(this);
    }

    public void setParentDirectory(Directory parentDirectory){
        this.parentDirectory=parentDirectory;
    }

    public void addChildDirectory(Directory child){
        childDirectories.add(child);
        child.setParentDirectory(this);
    }






}
