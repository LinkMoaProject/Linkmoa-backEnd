/*
package com.linkmoa.source.domain.search.service;

import com.linkmoa.source.domain.Favorite.entity.Favorite;
import com.linkmoa.source.domain.Favorite.repository.FavoriteRepository;
import com.linkmoa.source.domain.Favorite.service.FavoriteService;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.search.document.DirectoryDocument;
import com.linkmoa.source.domain.search.document.SiteDocument;
import com.linkmoa.source.domain.search.error.SearchErrorCode;
import com.linkmoa.source.domain.search.exception.SearchException;
import com.linkmoa.source.domain.search.repository.DirectoryElasticsearchRepository;
import com.linkmoa.source.domain.search.repository.SiteElasticsearchRepository;
import com.linkmoa.source.domain.site.entity.Site;
import com.linkmoa.source.domain.site.repository.SiteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchSyncService {


    private final DirectoryRepository directoryRepository;
    private final SiteRepository siteRepository;
    private final DirectoryElasticsearchRepository directoryElasticsearchRepository;
    private final SiteElasticsearchRepository siteElasticsearchRepository;
    private final FavoriteService favoriteService;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void syncDataToElasticsearch() {
        try {
            // 1. MySQL에서 모든 Directory와 Site 가져오기
            List<Directory> directories = directoryRepository.findAll();
            List<Site> sites = siteRepository.findAll();

            // 2. 전체 즐겨찾기 정보 가져오기 (특정 사용자가 아닌 전체 기준)
            List<Favorite> allFavorites = favoriteRepository.findAll();

            // 3. 즐겨찾기된 Directory ID와 Site ID 목록을 Set으로 변환
            Set<Long> favoriteDirectoryIds = favoriteService.findFavoriteDirectoryIds(allFavorites);
            Set<Long> favoriteSiteIds = favoriteService.findFavoriteSiteIds(allFavorites);

            // 4. Elasticsearch에 모든 Directory 저장
            for (Directory directory : directories) {
                try {
                    DirectoryDocument document = new DirectoryDocument(
                            directory.getId().toString(),
                            directory.getDirectoryName(),
                            directory.getOrderIndex(),
                            favoriteDirectoryIds.contains(directory.getId())
                    );
                    directoryElasticsearchRepository.save(document);
                } catch (Exception e) {
                    throw new SearchException(SearchErrorCode.SAVE_DIRECTORY_TO_ES_FAILED);
                }
            }

            // 5. Elasticsearch에 모든 Site 저장
            for (Site site : sites) {
                try {
                    SiteDocument document = new SiteDocument(
                            site.getId().toString(),
                            site.getSiteName(),
                            site.getSiteUrl(),
                            site.getOrderIndex(),
                            favoriteSiteIds.contains(site.getId())
                    );
                    siteElasticsearchRepository.save(document);
                } catch (Exception e) {
                    throw new SearchException(SearchErrorCode.SAVE_SITE_TO_ES_FAILED);
                }
            }

        } catch (Exception e) {
            throw new SearchException(SearchErrorCode.ES_SYNC_FAILED);        }
    }
}
*/
