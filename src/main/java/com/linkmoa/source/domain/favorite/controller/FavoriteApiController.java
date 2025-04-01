package com.linkmoa.source.domain.favorite.controller;

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
import com.linkmoa.source.domain.favorite.dto.request.FavoriteUpdateDto;
import com.linkmoa.source.domain.favorite.service.FavoriteService;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteApiController {

	private final FavoriteService favoriteService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<FavoriteUpdateDto.SimpleResponse>> updateFavorite(
		@RequestBody @Validated FavoriteUpdateDto.Request favoriteUpdateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiResponseSpec<FavoriteUpdateDto.SimpleResponse> response = favoriteService.updateFavorite(
			favoriteUpdateRequest, principalDetails);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<FavoriteUpdateDto.DetailResponse>> getFavorite(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiResponseSpec<FavoriteUpdateDto.DetailResponse> response = favoriteService.findFavoriteDetails(
			principalDetails);

		return ResponseEntity.ok().body(response);
	}

}
