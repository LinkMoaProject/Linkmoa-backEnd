package com.linkmoa.source.domain.search.repository;

import com.linkmoa.source.domain.search.document.DirectoryDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;



public interface DirectoryElasticsearchRepository extends ElasticsearchRepository<DirectoryDocument,String> {
    List<DirectoryDocument> findByTitleContaining(String title);
}
