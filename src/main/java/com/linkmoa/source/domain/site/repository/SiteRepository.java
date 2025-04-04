package com.linkmoa.source.domain.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkmoa.source.domain.site.entity.Site;

public interface SiteRepository extends JpaRepository<Site, Long>, SiteRepositoryCustom {

}
