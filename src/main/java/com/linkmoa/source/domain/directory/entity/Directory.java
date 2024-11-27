package com.linkmoa.source.domain.directory.entity;


import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.site.entity.Site;
import com.linkmoa.source.global.entity.BaseEntity;
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
public class Directory extends BaseEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="directory_id")
    private Long id;

    @Column(name="directory_name")
    private String directoryName;

    @Column(name="directory_description")
    private String directoryDescription;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="page_id")
    private Page page;


    @Builder
    public Directory(String directoryName,Directory parentDirectory,Page page,String directoryDescription){
        this.directoryName=directoryName;
        this.parentDirectory=parentDirectory;
        this.page=page;
        this.directoryDescription=directoryDescription;
    }


    public void setParentDirectory(Directory parentDirectory){
        this.parentDirectory=parentDirectory;
    }

    public void setPage(Page page){
        this.page = page;
        page.getDirectories().add(this);
    }

    public void addChildDirectory(Directory child){
        childDirectories.add(child);
        child.setParentDirectory(this);
    }

    public void updateDirectoryNameAndDescription(String directoryName,String directoryDescription){
        this.directoryName=directoryName;
        this.directoryDescription=directoryDescription;
    }






}
