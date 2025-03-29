package com.linkmoa.source.domain.search.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.search.dto.request.SearchRequest;
import com.linkmoa.source.domain.search.dto.response.SearchPageResponse;
import com.linkmoa.source.domain.search.service.SearchService;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@Slf4j
public class SearchApiController {

	private final SearchService searchService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiResponseSpec<SearchPageResponse>> getDirectoryAndSiteByKeyword(
		@RequestBody SearchRequest searchRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		log.debug("🔍 pageController Search 요청: pageId={}, keyword='{}', type={}",
			searchRequest.pageId(),
			searchRequest.keyword(),
			searchRequest.searchType());

		ApiResponseSpec<SearchPageResponse> apiSearchResponse = searchService.searchDirectoriesAndSitesByTitleInPage(
			searchRequest, principalDetails);

		return ResponseEntity.ok().body(apiSearchResponse);
	}
}
