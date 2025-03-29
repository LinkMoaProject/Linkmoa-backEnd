package com.linkmoa.source.domain.favorite.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.favorite.constant.ItemType;
import com.linkmoa.source.domain.favorite.dto.request.FavoriteUpdateRequest;
import com.linkmoa.source.domain.favorite.dto.response.ApiFavoriteResponseSpec;
import com.linkmoa.source.domain.favorite.dto.response.FavoriteDetailResponse;
import com.linkmoa.source.domain.favorite.dto.response.FavoriteResponse;
import com.linkmoa.source.domain.favorite.entity.Favorite;
import com.linkmoa.source.domain.favorite.error.FavoriteErrorCode;
import com.linkmoa.source.domain.favorite.exception.FavoriteException;
import com.linkmoa.source.domain.favorite.repository.FavoriteRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import com.linkmoa.source.domain.site.repository.SiteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final DirectoryRepository directoryRepository;
	private final SiteRepository siteRepository;

	@Transactional
	public ApiFavoriteResponseSpec<FavoriteResponse> updateFavorite(FavoriteUpdateRequest favoriteUpdateRequest,
		PrincipalDetails principalDetails) {
		Favorite favorite = favoriteRepository.findByItemIdAndItemType(favoriteUpdateRequest.itemId(),
			favoriteUpdateRequest.itemType());

		if (favorite == null) {
			return createFavorite(favoriteUpdateRequest, principalDetails);
		} else {
			return deleteFavorite(favorite);
		}
	}

	private ApiFavoriteResponseSpec<FavoriteResponse> createFavorite(FavoriteUpdateRequest favoriteUpdateRequest,
		PrincipalDetails principalDetails) {

		try {
			Member member = principalDetails.getMember();

			favoriteRepository.incrementOrderIndexesForMember(member);

			Favorite newFavorite = Favorite.builder()
				.member(member)
				.itemId(favoriteUpdateRequest.itemId())
				.itemType(favoriteUpdateRequest.itemType())
				.orderIndex(1)
				.build();

			favoriteRepository.save(newFavorite);
		} catch (Exception e) {
			throw new FavoriteException(FavoriteErrorCode.FAVORITE_DELETE_FAILED);
		}
		FavoriteResponse favoriteResponse = FavoriteResponse.builder()
			.itemType(favoriteUpdateRequest.itemType())
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
		} catch (Exception e) {
			throw new FavoriteException(FavoriteErrorCode.FAVORITE_DELETE_FAILED);
		}
		FavoriteResponse favoriteResponse = FavoriteResponse.builder()
			.itemId(favorite.getItemId())
			.itemType(favorite.getItemType())
			.build();

		return ApiFavoriteResponseSpec.<FavoriteResponse>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("아이템( 디렉토리, 사이트)를 즐겨 찾기에서 삭제했습니다.")
			.data(favoriteResponse)
			.build();
	}

	public List<Long> findFavoriteDirectoryIds(List<Favorite> favorites) {
		return favorites.stream()
			.filter(favorite -> favorite.getItemType() == ItemType.DIRECTORY)
			.map(favorite -> favorite.getItemId())
			.collect(Collectors.toList());
	}

	public List<Long> findFavoriteSiteIds(List<Favorite> favorites) {
		return favorites.stream()
			.filter(favorite -> favorite.getItemType() == ItemType.SITE)
			.map(favorite -> favorite.getItemId())
			.collect(Collectors.toList());
	}

	public ApiFavoriteResponseSpec<FavoriteDetailResponse> findFavoriteDetails(PrincipalDetails principalDetails) {
		List<Favorite> favorites = favoriteRepository.findByMember(principalDetails.getMember());

		List<Long> favoriteDirectoryIds = findFavoriteDirectoryIds(favorites);
		List<Long> favoriteSiteIds = findFavoriteSiteIds(favorites);

		List<DirectoryDetailResponse> directoryDetailResponses = directoryRepository.findFavoriteDirectories(
			favoriteDirectoryIds);
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
