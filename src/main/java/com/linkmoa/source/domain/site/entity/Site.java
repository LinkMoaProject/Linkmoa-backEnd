package com.linkmoa.source.domain.site.entity;


import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="sites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Site extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="site_id")
    private Long id;

    @Column(name="site_name")
    private String siteName;

    @Column(name="site_url")
    private String siteUrl;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name="directory_id")
    private Directory directory;

    @Column(name="order_index")
    private Integer orderIndex;

    @Builder
    public Site(String siteName,String siteUrl,Directory directory,Integer orderIndex){
        this.siteName=siteName;
        this.siteUrl=siteUrl;
        this.orderIndex=orderIndex;
        setDirectory(directory);

    }

    public void setDirectory(Directory directory) {
        if (this.directory != null) {
            this.directory.getSites().remove(this);
        }

        this.directory = directory;
        directory.getSites().add(this);
    }

    public void updateSiteNameAndUrl(String siteName,String siteUrl){
        this.siteName=siteName;
        this.siteUrl=siteUrl;
    }




}
