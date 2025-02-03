package com.linkmoa.source.domain.Favorite.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.Favorite.constant.FavoriteType;
import com.linkmoa.source.domain.Favorite.dto.request.FavoriteUpdateRequest;
import com.linkmoa.source.domain.Favorite.dto.response.ApiFavoriteResponseSpec;
import com.linkmoa.source.domain.Favorite.dto.response.FavoriteDetailResponse;
import com.linkmoa.source.domain.Favorite.dto.response.FavoriteResponse;
import com.linkmoa.source.domain.Favorite.entity.Favorite;
import com.linkmoa.source.domain.Favorite.error.FavoriteErrorCode;
import com.linkmoa.source.domain.Favorite.exception.FavoriteException;
import com.linkmoa.source.domain.Favorite.repository.FavoriteRepository;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import com.linkmoa.source.domain.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final DirectoryRepository directoryRepository;
    private final SiteRepository siteRepository;
    @Transactional
    public ApiFavoriteResponseSpec<FavoriteResponse> updateFavorite(FavoriteUpdateRequest favoriteUpdateRequest,PrincipalDetails principalDetails){
        Favorite favorite = favoriteRepository.findByItemIdAndFavoriteType(favoriteUpdateRequest.itemId(), favoriteUpdateRequest.favoriteType());

        if(favorite==null) {
        return createFavorite(favoriteUpdateRequest, principalDetails);
        }
        else{
            return deleteFavorite(favorite);
        }
    }


    private ApiFavoriteResponseSpec<FavoriteResponse> createFavorite(FavoriteUpdateRequest favoriteUpdateRequest, PrincipalDetails principalDetails){


        try{
            Member member = principalDetails.getMember();

        favoriteRepository.incrementOrderIndexesForMember(member);

        Favorite newFavorite = Favorite.builder()
                .member(member)
                .itemId(favoriteUpdateRequest.itemId())
                .favoriteType(favoriteUpdateRequest.favoriteType())
                .orderIndex(1)
                .build();

        favoriteRepository.save(newFavorite);
        }catch (Exception e){
            throw new FavoriteException(FavoriteErrorCode.FAVORITE_DELETE_FAILED);
        }
        FavoriteResponse favoriteResponse = FavoriteResponse.builder()
                .favoriteType(favoriteUpdateRequest.favoriteType())
                .itemId(favoriteUpdateRequest.itemId())
                .build();


        return ApiFavoriteResponseSpec.<FavoriteResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("아이템 ( 디렉토리 , 사이트)를 즐겨 찾기에 등록했습니다.")
                .data(favoriteResponse)
                .build();

    }

    private ApiFavoriteResponseSpec<FavoriteResponse> deleteFavorite(Favorite favorite) {

        try {
            favoriteRepository.decrementFavoriteOrderIndexes(favorite.getOrderIndex());
            favoriteRepository.delete(favorite);
        }catch (Exception e){
            throw new FavoriteException(FavoriteErrorCode.FAVORITE_DELETE_FAILED);
        }
        FavoriteResponse favoriteResponse = FavoriteResponse.builder()
                .itemId(favorite.getItemId())
                .favoriteType(favorite.getFavoriteType())
                .build();

        return ApiFavoriteResponseSpec.<FavoriteResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("아이템( 디렉토리, 사이트)를 즐겨 찾기에서 삭제했습니다.")
                .data(favoriteResponse)
                .build();
    }
    public Set<Long> findFavoriteDirectoryIds(List<Favorite> favorites) {
        return favorites.stream()
                .filter(favorite -> favorite.getFavoriteType() == FavoriteType.DIRECTORY)
                .map(favorite -> favorite.getItemId())
                .collect(Collectors.toSet());
    }

    public Set<Long> findFavoriteSiteIds(List<Favorite> favorites) {
        return favorites.stream()
                .filter(favorite -> favorite.getFavoriteType() == FavoriteType.SITE)
                .map(favorite -> favorite.getItemId())
                .collect(Collectors.toSet());
    }


    public ApiFavoriteResponseSpec<FavoriteDetailResponse> findFavoriteDetails(PrincipalDetails principalDetails){
        List<Favorite> favorites = favoriteRepository.findByMember(principalDetails.getMember());

        Set<Long> favoriteDirectoryIds = findFavoriteDirectoryIds(favorites);
        Set<Long> favoriteSiteIds = findFavoriteSiteIds(favorites);


        List<DirectoryDetailResponse> directoryDetailResponses = directoryRepository.findFavoriteDirectories(favoriteDirectoryIds);
        List<SiteDetailResponse> sitesDetails = siteRepository.findFavoriteSites(favoriteSiteIds);

        FavoriteDetailResponse favoriteDetailResponse = FavoriteDetailResponse.builder()
                .email(principalDetails.getEmail())
                .directoryDetailResponses(directoryDetailResponses)
                .siteDetailResponses(sitesDetails)
                .build();

        return ApiFavoriteResponseSpec.<FavoriteDetailResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("즐겨찾기를 조회했습니다.")
                .data(favoriteDetailResponse)
                .build();
    }

}
