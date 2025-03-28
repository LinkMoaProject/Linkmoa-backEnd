package com.linkmoa.source.domain.favorite.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.favorite.dto.request.FavoriteUpdateRequest;
import com.linkmoa.source.domain.favorite.dto.response.ApiFavoriteResponseSpec;
import com.linkmoa.source.domain.favorite.dto.response.FavoriteDetailResponse;
import com.linkmoa.source.domain.favorite.dto.response.FavoriteResponse;
import com.linkmoa.source.domain.favorite.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteApiController {

	private final FavoriteService favoriteService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiFavoriteResponseSpec<FavoriteResponse>> updateFavorite(
		@RequestBody @Validated FavoriteUpdateRequest favoriteUpdateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiFavoriteResponseSpec<FavoriteResponse> favoriteCreateResponse = favoriteService.updateFavorite(
			favoriteUpdateRequest, principalDetails);

		return ResponseEntity.ok().body(favoriteCreateResponse);
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiFavoriteResponseSpec<FavoriteDetailResponse>> getFavorite(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiFavoriteResponseSpec<FavoriteDetailResponse> favoriteDetails = favoriteService.findFavoriteDetails(
			principalDetails);

		return ResponseEntity.ok().body(favoriteDetails);
	}

}
