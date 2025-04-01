package com.linkmoa.source.domain.favorite.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.DirectorySimpleResponse;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.favorite.constant.ItemType;
import com.linkmoa.source.domain.favorite.dto.request.FavoriteUpdateDto;
import com.linkmoa.source.domain.favorite.entity.Favorite;
import com.linkmoa.source.domain.favorite.error.FavoriteErrorCode;
import com.linkmoa.source.domain.favorite.exception.FavoriteException;
import com.linkmoa.source.domain.favorite.repository.FavoriteRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.site.dto.response.SiteSimpleResponse;
import com.linkmoa.source.domain.site.repository.SiteRepository;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final DirectoryRepository directoryRepository;
	private final SiteRepository siteRepository;

	@Transactional
	public ApiResponseSpec<FavoriteUpdateDto.SimpleResponse> updateFavorite(FavoriteUpdateDto.Request request,
		PrincipalDetails principalDetails) {
		Favorite favorite = favoriteRepository.findByItemIdAndItemType(request.itemId(),
			request.itemType());

		if (favorite == null) {
			return createFavorite(request, principalDetails);
		} else {
			return deleteFavorite(favorite);
		}
	}

	private ApiResponseSpec<FavoriteUpdateDto.SimpleResponse> createFavorite(FavoriteUpdateDto.Request request,
		PrincipalDetails principalDetails) {

		try {
			Member member = principalDetails.getMember();

			favoriteRepository.incrementOrderIndexesForMember(member);

			Favorite newFavorite = Favorite.builder()
				.member(member)
				.itemId(request.itemId())
				.itemType(request.itemType())
				.orderIndex(1)
				.build();

			favoriteRepository.save(newFavorite);
		} catch (Exception e) {
			throw new FavoriteException(FavoriteErrorCode.FAVORITE_CREATE_FAILED);
		}
		FavoriteUpdateDto.SimpleResponse response = FavoriteUpdateDto.SimpleResponse.builder()
			.itemType(request.itemType())
			.itemId(request.itemId())
			.build();

		return ApiResponseSpec.success(
			HttpStatus.OK,
			"아이템 ( 디렉토리 , 사이트)를 즐겨 찾기에 등록했습니다.",
			response
		);

	}

	private ApiResponseSpec<FavoriteUpdateDto.SimpleResponse> deleteFavorite(Favorite favorite) {

		try {
			favoriteRepository.decrementFavoriteOrderIndexes(favorite.getOrderIndex());
			favoriteRepository.delete(favorite);
		} catch (Exception e) {
			throw new FavoriteException(FavoriteErrorCode.FAVORITE_DELETE_FAILED);
		}
		FavoriteUpdateDto.SimpleResponse response = FavoriteUpdateDto.SimpleResponse.builder()
			.itemId(favorite.getItemId())
			.itemType(favorite.getItemType())
			.build();

		return ApiResponseSpec.success(
			HttpStatus.OK,
			"아이템( 디렉토리, 사이트)를 즐겨 찾기에서 삭제했습니다.",
			response
		);
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

	public ApiResponseSpec<FavoriteUpdateDto.DetailResponse> findFavoriteDetails(PrincipalDetails principalDetails) {
		List<Favorite> favorites = favoriteRepository.findByMember(principalDetails.getMember());

		List<Long> favoriteDirectoryIds = findFavoriteDirectoryIds(favorites);
		List<Long> favoriteSiteIds = findFavoriteSiteIds(favorites);

		List<DirectorySimpleResponse> directoryDetailResponses = directoryRepository.findFavoriteDirectories(
			favoriteDirectoryIds);
		List<SiteSimpleResponse> sitesDetails = siteRepository.findFavoriteSites(favoriteSiteIds);

		FavoriteUpdateDto.DetailResponse response = FavoriteUpdateDto.DetailResponse.builder()
			.email(principalDetails.getEmail())
			.directorySimpleResponses(directoryDetailResponses)
			.siteSimpleResponses(sitesDetails)
			.build();

		return ApiResponseSpec.success(
			HttpStatus.OK,
			"즐겨찾기를 조회했습니다.",
			response
		);
	}

}
