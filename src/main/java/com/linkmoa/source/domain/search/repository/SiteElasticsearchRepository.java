package com.linkmoa.source.domain.search.repository;

import com.linkmoa.source.domain.search.document.SiteDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SiteElasticsearchRepository extends ElasticsearchRepository<SiteDocument,String> {
    List<SiteDocument> findByTitleContaining(String title);
}
