/*
package com.linkmoa.source.domain.search.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.Favorite.entity.Favorite;
import com.linkmoa.source.domain.Favorite.repository.FavoriteRepository;
import com.linkmoa.source.domain.Favorite.service.FavoriteService;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryResponse;
import com.linkmoa.source.domain.search.document.DirectoryDocument;
import com.linkmoa.source.domain.search.document.SiteDocument;
import com.linkmoa.source.domain.search.repository.DirectoryElasticsearchRepository;
import com.linkmoa.source.domain.search.repository.SiteElasticsearchRepository;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class SearchService {


    private final DirectoryElasticsearchRepository directoryElasticsearchRepository;
    private final SiteElasticsearchRepository siteElasticsearchRepository;
    private final FavoriteService favoriteService;
    private final FavoriteRepository favoriteRepository;
    private final SearchSyncService searchSyncService;

    */
/**
     *  제목으로 검색하여 DirectoryResponse 형태로 변환
     * @param title
     * @param principalDetails
     * @return
     *//*


    @Transactional
    public ApiDirectoryResponseSpec<DirectoryResponse> searchByTitle(String title, PrincipalDetails principalDetails) {

        searchSyncService.syncDataToElasticsearch();


        List<DirectoryDocument> directoryResults = directoryElasticsearchRepository.findByTitleContaining(title);
        List<SiteDocument> siteResults = siteElasticsearchRepository.findByTitleContaining(title);

        List<Favorite> favorites = favoriteRepository.findByMember(principalDetails.getMember());
        Set<Long> favoriteDirectoryIds = favoriteService.findFavoriteDirectoryIds(favorites);
        Set<Long> favoriteSiteIds = favoriteService.findFavoriteSiteIds(favorites);

        List<DirectoryDetailResponse> directoryDetails = directoryResults.stream()
                .map(doc -> DirectoryDetailResponse.from(doc, favoriteDirectoryIds))
                .collect(Collectors.toList());

        List<SiteDetailResponse> siteDetails = siteResults.stream()
                .map(doc -> SiteDetailResponse.from(doc, favoriteSiteIds))
                .collect(Collectors.toList());


        DirectoryResponse directoryResponse = DirectoryResponse.builder()
                .directoryDetailResponses(directoryDetails)
                .siteDetailResponses(siteDetails)
                .build();

        return ApiDirectoryResponseSpec.<DirectoryResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("디렉토리와 사이트를 조회했습니다.")
                .data(directoryResponse)
                .build();
    }

}
*/
