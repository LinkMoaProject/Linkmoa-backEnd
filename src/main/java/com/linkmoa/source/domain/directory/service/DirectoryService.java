package com.linkmoa.source.domain.directory.service;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.Favorite.entity.Favorite;
import com.linkmoa.source.domain.Favorite.repository.FavoriteRepository;
import com.linkmoa.source.domain.Favorite.service.FavoriteService;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryDragAndDropRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryIdRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryMoveRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryPasteRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateRequest;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDragAndDropResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryPasteResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryResponse;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import com.linkmoa.source.domain.site.entity.Site;
import com.linkmoa.source.domain.site.error.SiteErrorCode;
import com.linkmoa.source.domain.site.exception.SiteException;
import com.linkmoa.source.domain.site.repository.SiteRepository;
import com.linkmoa.source.global.aop.annotation.ValidationApplied;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectoryService {

	private final DirectoryRepository directoryRepository;
	private final SiteRepository siteRepository;
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;

	@Transactional
	@ValidationApplied
	public ApiDirectoryResponseSpec<Long> createDirectory(DirectoryCreateRequest requestDto,
		PrincipalDetails principalDetails) {

		Directory parentDirectory = requestDto.parentDirectoryId() == null
			? null
			: directoryRepository.findById(requestDto.parentDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		Integer nextOrderIndex = parentDirectory.getNextOrderIndex();

		Directory newDirectory = Directory.builder()
			.directoryName(requestDto.directoryName())
			.directoryDescription(requestDto.directoryDescription())
			.orderIndex(nextOrderIndex)
			.build();

		// 부모 디렉토리에 새 디렉토리 추가
		if (parentDirectory != null) {
			parentDirectory.addChildDirectory(newDirectory);
		}

		directoryRepository.save(newDirectory);

		return ApiDirectoryResponseSpec.<Long>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("Directory 생성에 성공했습니다.")
			.data(newDirectory.getId())
			.build();
	}

	@Transactional
	@ValidationApplied
	public ApiDirectoryResponseSpec<Long> updateDirectory(DirectoryUpdateRequest requestDto,
		PrincipalDetails principalDetails) {

		Directory updateDirectory = directoryRepository.findById(requestDto.directoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		updateDirectory.updateDirectoryNameAndDescription(requestDto.directoryName(),
			requestDto.directoryDescription());

		return ApiDirectoryResponseSpec.<Long>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("Directory 수정(이름,설명)에 성공했습니다.")
			.data(updateDirectory.getId())
			.build();

	}

	@Transactional
	@ValidationApplied
	public ApiDirectoryResponseSpec<Long> deleteDirectory(DirectoryIdRequest requestDto,
		PrincipalDetails principalDetails) {

		Directory deleteDirectory = directoryRepository.findById(requestDto.directoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		Long directoryId = deleteDirectory.getId();
		Integer orderIndex = deleteDirectory.getOrderIndex();
		Directory parentDirectory = deleteDirectory.getParentDirectory();

		directoryRepository.decrementDirectoryAndSiteOrderIndexes(parentDirectory, orderIndex);
		directoryRepository.delete(deleteDirectory);

		return ApiDirectoryResponseSpec.<Long>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("Directory 삭제에 성공했습니다.")
			.data(directoryId)
			.build();
	}

	@Transactional
	@ValidationApplied
	public ApiDirectoryResponseSpec<Long> moveDirectory(DirectoryMoveRequest requestDto,
		PrincipalDetails principalDetails) {

		Directory sourceDirectory = directoryRepository.findById(requestDto.sourceDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		Directory targetDirectory = directoryRepository.findById(requestDto.targetDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		sourceDirectory.setParentDirectory(targetDirectory);

		directoryRepository.decrementDirectoryAndSiteOrderIndexes(sourceDirectory, sourceDirectory.getOrderIndex());

		Integer newOrderIndex = targetDirectory.getNextOrderIndex();
		sourceDirectory.setOrderIndex(newOrderIndex);

		return ApiDirectoryResponseSpec.<Long>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("Directory를 다른 Directory로 이동시켰습니다.")
			.data(sourceDirectory.getId())
			.build();
	}

	@Transactional
	@ValidationApplied
	public ApiDirectoryResponseSpec<DirectoryDragAndDropResponse> dragAndDropDirectoryOrSite(
		DirectoryDragAndDropRequest directoryDragAndDropRequest,
		PrincipalDetails principalDetails) {

		Integer currentItemOrderIndex;

		Directory parentDirectory = directoryRepository.findById(directoryDragAndDropRequest.parentDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		currentItemOrderIndex = getOrderIndex(
			directoryDragAndDropRequest.targetId(),
			directoryDragAndDropRequest.targetType()
		);

		boolean isIncrement = currentItemOrderIndex > directoryDragAndDropRequest.targetOrderIndex();
		int startIndex = Math.min(currentItemOrderIndex, directoryDragAndDropRequest.targetOrderIndex());
		int endIndex = Math.max(currentItemOrderIndex, directoryDragAndDropRequest.targetOrderIndex());

		directoryRepository.updateDirectoryAndSiteOrderIndexesInRange(parentDirectory, startIndex, endIndex,
			isIncrement);

		setOrderIndex(
			directoryDragAndDropRequest.targetId(),
			directoryDragAndDropRequest.targetType(),
			directoryDragAndDropRequest.targetOrderIndex()
		);

		DirectoryDragAndDropResponse directoryDragAndDropResponse = DirectoryDragAndDropResponse.builder()
			.targetId(directoryDragAndDropRequest.targetId())
			.targetType(String.valueOf(directoryDragAndDropRequest.targetType()))
			.targetOrderIndex(directoryDragAndDropRequest.targetOrderIndex())
			.build();

		return ApiDirectoryResponseSpec.<DirectoryDragAndDropResponse>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("Drag and Drop를 수행했습니다.")
			.data(directoryDragAndDropResponse)
			.build();
	}

	private Integer getOrderIndex(Long targetId, DirectoryDragAndDropRequest.TargetType targetType) {
		switch (targetType) {
			case DIRECTORY -> {
				Directory directory = directoryRepository.findById(targetId)
					.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));
				return directory.getOrderIndex();
			}
			case SITE -> {
				Site site = siteRepository.findById(targetId)
					.orElseThrow(() -> new SiteException(SiteErrorCode.SITE_NOT_FOUND));
				return site.getOrderIndex();
			}
			default -> throw new DirectoryException(DirectoryErrorCode.UNSUPPORTED_TARGET_TYPE);
		}
	}

	private void setOrderIndex(Long targetId, DirectoryDragAndDropRequest.TargetType targetType,
		Integer targetOrderIndex) {
		switch (targetType) {
			case DIRECTORY -> {
				Directory directory = directoryRepository.findById(targetId)
					.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));
				directory.setOrderIndex(targetOrderIndex);
			}
			case SITE -> {
				Site site = siteRepository.findById(targetId)
					.orElseThrow(() -> new SiteException(SiteErrorCode.SITE_NOT_FOUND));
				site.setOrderIndex(targetOrderIndex);
			}
			default -> throw new DirectoryException(DirectoryErrorCode.UNSUPPORTED_TARGET_TYPE);
		}
	}

	@ValidationApplied
	public ApiDirectoryResponseSpec<DirectoryResponse> findDirectoryDetails(DirectoryIdRequest directoryIdRequest
		, PrincipalDetails principalDetails) {

		Directory targetDirectory = directoryRepository.findById(directoryIdRequest.directoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		List<Favorite> favorites = favoriteRepository.findByMember(principalDetails.getMember());

		Set<Long> favoriteDirectoryIds = favoriteService.findFavoriteDirectoryIds(favorites);
		Set<Long> favoriteSiteIds = favoriteService.findFavoriteSiteIds(favorites);

		List<DirectoryDetailResponse> directoryDetailResponses = directoryRepository.findDirectoryDetails(
			targetDirectory.getId(), favoriteDirectoryIds);

		List<SiteDetailResponse> sitesDetails = siteRepository.findSitesDetails(targetDirectory.getId(),
			favoriteSiteIds);

		DirectoryResponse directoryResponse = DirectoryResponse.builder()
			.targetDirectoryDescription(targetDirectory.getDirectoryDescription())
			.targetDirectoryName(targetDirectory.getDirectoryName())
			.directoryDetailResponses(directoryDetailResponses)
			.siteDetailResponses(sitesDetails)
			.build();

		return ApiDirectoryResponseSpec.<DirectoryResponse>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("Directory 클릭 시, 해당 디렉토리 내에 사이트 및 디렉토리를 조회했습니다.")
			.data(directoryResponse)
			.build();

	}

	@ValidationApplied
	@Transactional
	public ApiDirectoryResponseSpec<DirectoryPasteResponse> pasteDirectory(DirectoryPasteRequest directoryPasteRequest,
		PrincipalDetails principalDetails) {
		Directory originalDirectory = directoryRepository.findById(directoryPasteRequest.originalDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		Directory destinationDirectory = directoryRepository.findById(directoryPasteRequest.destinationDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		directoryRepository.incrementDirectoryAndSiteOrderIndexes(destinationDirectory, 0);

		Directory pastedDirectory = originalDirectory.cloneDirectory(destinationDirectory);
		pastedDirectory.setOrderIndex(1);
		directoryRepository.save(pastedDirectory);

		DirectoryPasteResponse directoryPasteResponse = DirectoryPasteResponse.builder()
			.pastedirectoryId(pastedDirectory.getId())
			.destinationDirectoryId(destinationDirectory.getId())
			.clonedDirectoryName(pastedDirectory.getDirectoryName())
			.build();

		return ApiDirectoryResponseSpec.<DirectoryPasteResponse>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage("Directory 붙여넣기에 성공했습니다.")
			.data(directoryPasteResponse)
			.build();
	}

	public Directory cloneDirectory(Long newRootDirectoryId, Long originalDirectoryId) {

		Directory originalDirectory = directoryRepository.findById(originalDirectoryId)
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		Directory newParentDirectory = directoryRepository.findById(newRootDirectoryId).orElse(null);

		directoryRepository.incrementDirectoryAndSiteOrderIndexes(newParentDirectory, 0);

		Directory clonedDirectory = originalDirectory.cloneDirectory(newParentDirectory);
		clonedDirectory.setOrderIndex(1);
		directoryRepository.save(clonedDirectory);
		return clonedDirectory;
	}
}
