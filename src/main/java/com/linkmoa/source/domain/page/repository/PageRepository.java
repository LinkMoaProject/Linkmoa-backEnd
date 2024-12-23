package com.linkmoa.source.domain.page.repository;

import com.linkmoa.source.domain.page.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page,Long>,PageRepositoryCustom {
}
