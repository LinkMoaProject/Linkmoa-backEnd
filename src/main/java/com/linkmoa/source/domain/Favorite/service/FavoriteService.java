package com.linkmoa.source.domain.Favorite.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.Favorite.dto.request.FavoriteCreateRequest;
import com.linkmoa.source.domain.Favorite.dto.response.ApiFavoriteResponseSpec;
import com.linkmoa.source.domain.Favorite.dto.response.FavoriteCreateResponse;
import com.linkmoa.source.domain.Favorite.entity.Favorite;
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
    public ApiFavoriteResponseSpec<FavoriteCreateResponse> createFavorite(FavoriteCreateRequest favoriteCreateRequest, PrincipalDetails principalDetails){
        Member member = principalDetails.getMember();


        favoriteRepository.incrementOrderIndexesForMember(member);

        Favorite newFavorite = Favorite.builder()
                .member(member)
                .itemId(favoriteCreateRequest.itemId())
                .favoriteType(favoriteCreateRequest.favoriteType())
                .orderIndex(1)
                .build();

        favoriteRepository.save(newFavorite);

        FavoriteCreateResponse favoriteCreateResponse = FavoriteCreateResponse.builder()
                .favoriteType(favoriteCreateRequest.favoriteType())
                .itemId(favoriteCreateRequest.itemId())
                .build();


        return ApiFavoriteResponseSpec.<FavoriteCreateResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("아이템 ( 디렉토리 , 사이트)를 즐겨 찾기에 등록했습니다.")
                .data(favoriteCreateResponse)
                .build();

    }

}
