package com.linkmoa.source.domain.Favorite.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.Favorite.dto.request.FavoriteUpdateRequest;
import com.linkmoa.source.domain.Favorite.dto.response.ApiFavoriteResponseSpec;
import com.linkmoa.source.domain.Favorite.dto.response.FavoriteResponse;
import com.linkmoa.source.domain.Favorite.entity.Favorite;
import com.linkmoa.source.domain.Favorite.error.FavoriteErrorCode;
import com.linkmoa.source.domain.Favorite.exception.FavoriteException;
import com.linkmoa.source.domain.Favorite.repository.FavoriteRepository;
import com.linkmoa.source.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

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

}
