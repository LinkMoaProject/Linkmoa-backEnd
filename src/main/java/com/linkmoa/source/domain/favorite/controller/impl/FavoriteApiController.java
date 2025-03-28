package com.linkmoa.source.domain.favorite.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.favorite.controller.spec.FavoriteApiSpecification;
import com.linkmoa.source.domain.favorite.dto.request.FavoriteUpdateRequest;
import com.linkmoa.source.domain.favorite.dto.response.ApiFavoriteResponseSpec;
import com.linkmoa.source.domain.favorite.dto.response.FavoriteDetailResponse;
import com.linkmoa.source.domain.favorite.dto.response.FavoriteResponse;
import com.linkmoa.source.domain.favorite.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteApiController implements FavoriteApiSpecification {

	private final FavoriteService favoriteService;

	public ResponseEntity<ApiFavoriteResponseSpec<FavoriteResponse>> updateFavorite(
		FavoriteUpdateRequest favoriteUpdateRequest,
		PrincipalDetails principalDetails) {

		ApiFavoriteResponseSpec<FavoriteResponse> favoriteCreateResponse = favoriteService.updateFavorite(
			favoriteUpdateRequest, principalDetails);

		return ResponseEntity.ok().body(favoriteCreateResponse);
	}

	public ResponseEntity<ApiFavoriteResponseSpec<FavoriteDetailResponse>> getFavorite(
		PrincipalDetails principalDetails) {

		ApiFavoriteResponseSpec<FavoriteDetailResponse> favoriteDetails = favoriteService.findFavoriteDetails(
			principalDetails);

		return ResponseEntity.ok().body(favoriteDetails);
	}

}
