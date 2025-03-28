package com.linkmoa.source.domain.search.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.DirectorySimpleResponse;
import com.linkmoa.source.domain.page.repository.PageRepository;
import com.linkmoa.source.domain.search.dto.request.SearchRequest;
import com.linkmoa.source.domain.search.dto.response.ApiSearchResponseSpec;
import com.linkmoa.source.domain.search.dto.response.SearchPageResponse;
import com.linkmoa.source.domain.site.dto.response.SiteSimpleResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

	private final PageRepository pageRepository;

	public ApiSearchResponseSpec<SearchPageResponse> searchDirectoriesAndSitesByTitleInPage(SearchRequest searchRequest,
		PrincipalDetails principalDetails) {
		Long rootDirectoryIdByPageId = pageRepository.findRootDirectoryIdByPageId(searchRequest.pageId());

		log.debug("🔍 pageSerivce Search 요청: pageId={}, keyword='{}', type={}",
			searchRequest.pageId(),
			searchRequest.keyword(),
			searchRequest.searchType());

		List<Object[]> directoriesAndSitesByKeyword = pageRepository.findDirectoriesAndSitesByNameKeyword(
			searchRequest.keyword(),
			rootDirectoryIdByPageId,
			principalDetails.getId());
		log.debug("🔍 pageRepository Search 요청: ");

		List<DirectorySimpleResponse> directories = mapToDirectoryResponses(
			directoriesAndSitesByKeyword);

		List<SiteSimpleResponse> sites = mapToSiteResponses(directoriesAndSitesByKeyword);

		SearchPageResponse searchPageResponse = SearchPageResponse.builder()
			.directorySimpleResponses(directories)
			.siteSimpleResponses(sites)
			.build();

		return ApiSearchResponseSpec.<SearchPageResponse>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("사이트 내에 있는 모든 디렉토리와 사이트를 제목으로 검색합니다.")
			.data(searchPageResponse)
			.build();
	}

	private List<DirectorySimpleResponse> mapToDirectoryResponses(List<Object[]> rawResults) {
		List<DirectorySimpleResponse> directories = new ArrayList<>();

		for (Object[] row : rawResults) {
			Long directoryId = (Long)row[0];
			String directoryName = (String)row[1];
			Boolean isDirectoryFavorite = (Boolean)row[2];

			directories.add(
				DirectorySimpleResponse.builder()
					.directoryId(directoryId)
					.directoryName(directoryName)
					.isFavorite(isDirectoryFavorite)
					.build()
			);
		}

		return directories;
	}

	private List<SiteSimpleResponse> mapToSiteResponses(List<Object[]> rawResults) {
		List<SiteSimpleResponse> sites = new ArrayList<>();

		for (Object[] row : rawResults) {
			Long siteId = (Long)row[3];
			String siteName = (String)row[4];
			String siteUrl = (String)row[5];
			Boolean isSiteFavorite = (Boolean)row[6];

			sites.add(
				SiteSimpleResponse.builder()
					.siteId(siteId)
					.siteName(siteName)
					.siteUrl(siteUrl)
					.isFavorite(isSiteFavorite)
					.build()
			);

		}
		return sites;
	}
}
