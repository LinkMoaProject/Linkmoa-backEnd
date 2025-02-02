package com.linkmoa.source.domain.Favorite.controller.spec;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.Favorite.dto.request.FavoriteCreateRequest;
import com.linkmoa.source.domain.Favorite.dto.response.ApiFavoriteResponseSpec;
import com.linkmoa.source.domain.Favorite.dto.response.FavoriteResponse;
import com.linkmoa.source.domain.Favorite.error.FavoriteErrorCode;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface FavoriteApiSpecification {


    @Tag(name = "Favorite", description = "즐겨찾기 관련 API")
    @Operation(summary = "즐겨찾기 생성", description = "아이템(디레토리,사이트)를 즐겨찾기에 등록합니다.")
    @ApiErrorCodeExamples(FavoriteErrorCode.class)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<ApiFavoriteResponseSpec<FavoriteResponse>> createFavorite(
            @RequestBody @Validated FavoriteCreateRequest favoriteCreateRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );
}
