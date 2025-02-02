package com.linkmoa.source.domain.Favorite.controller.impl;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.Favorite.controller.spec.FavoriteApiSpecification;
import com.linkmoa.source.domain.Favorite.dto.request.FavoriteCreateRequest;
import com.linkmoa.source.domain.Favorite.dto.response.ApiFavoriteResponseSpec;
import com.linkmoa.source.domain.Favorite.dto.response.FavoriteResponse;
import com.linkmoa.source.domain.Favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteApiController implements FavoriteApiSpecification {

    private final FavoriteService favoriteService;

    @Override
    public ResponseEntity<ApiFavoriteResponseSpec<FavoriteResponse>> createFavorite(
            FavoriteCreateRequest favoriteCreateRequest,
            PrincipalDetails principalDetails) {

        ApiFavoriteResponseSpec<FavoriteResponse> favoriteCreateResponse = favoriteService.createFavorite(favoriteCreateRequest, principalDetails);

        return ResponseEntity.ok().body(favoriteCreateResponse);
    }
}
