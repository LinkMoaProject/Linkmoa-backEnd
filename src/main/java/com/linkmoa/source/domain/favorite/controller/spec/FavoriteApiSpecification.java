package com.linkmoa.source.domain.favorite.controller.spec;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.favorite.dto.request.FavoriteUpdateRequest;
import com.linkmoa.source.domain.favorite.dto.response.ApiFavoriteResponseSpec;
import com.linkmoa.source.domain.favorite.dto.response.FavoriteDetailResponse;
import com.linkmoa.source.domain.favorite.dto.response.FavoriteResponse;
import com.linkmoa.source.domain.favorite.error.FavoriteErrorCode;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

public interface FavoriteApiSpecification {

	@Tag(name = "Favorite", description = "즐겨찾기 관련 API")
	@Operation(summary = "즐겨찾기 업데이트", description = "아이템(디레토리,사이트)이 즐겨찾기에 등록되지 않은 상태면, 즐겨찾기로 등록합니다. 하지만, 해당 아이템이 이미 즐겨찾기에 등록되어 있다면 즐겨찾기에서 삭제합니다.")
	@ApiErrorCodeExamples(FavoriteErrorCode.class)
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiFavoriteResponseSpec<FavoriteResponse>> updateFavorite(
		@RequestBody @Validated FavoriteUpdateRequest favoriteUpdateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

	@Tag(name = "Favorite", description = "즐겨찾기 관련 API")
	@Operation(summary = "즐겨찾기 조회", description = "즐겨찾기한 디렉토리와 사이트 조회합니다.")
	@ApiErrorCodeExamples(FavoriteErrorCode.class)
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiFavoriteResponseSpec<FavoriteDetailResponse>> getFavorite(
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

}
