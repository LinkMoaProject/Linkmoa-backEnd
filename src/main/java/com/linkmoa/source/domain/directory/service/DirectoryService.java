package com.linkmoa.source.domain.directory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryChangeParentDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryDragAndDropDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryIdDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryPasteDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateDto;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.favorite.constant.ItemType;
import com.linkmoa.source.domain.favorite.entity.Favorite;
import com.linkmoa.source.domain.favorite.repository.FavoriteRepository;
import com.linkmoa.source.domain.favorite.service.FavoriteService;
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
	public Long createDirectory(DirectoryCreateDto.Request request,
		PrincipalDetails principalDetails) {

		Directory parentDirectory = null;
		Integer nextOrderIndex;

		if (request.parentDirectoryId() != null) {
			parentDirectory = directoryRepository.findById(request.parentDirectoryId())
				.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));
			nextOrderIndex = parentDirectory.getNextOrderIndex();
		} else {
			nextOrderIndex = 1; // 또는 적절한 기본값
		}

		Directory newDirectory = Directory.builder()
			.directoryName(request.directoryName())
			.directoryDescription(request.directoryDescription())
			.orderIndex(nextOrderIndex)
			.build();

		// 부모 디렉토리에 새 디렉토리 추가
		if (parentDirectory != null) {
			parentDirectory.addChildDirectory(newDirectory);
		}

		directoryRepository.save(newDirectory);

		return newDirectory.getId();

	}

	@Transactional
	@ValidationApplied
	public Long updateDirectory(DirectoryUpdateDto.Request request,
		PrincipalDetails principalDetails) {

		Directory updateDirectory = directoryRepository.findById(request.directoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		updateDirectory.updateDirectoryNameAndDescription(request.directoryName(),
			request.directoryDescription());

		return updateDirectory.getId();
	}

	@Transactional
	@ValidationApplied
	public Long deleteDirectory(DirectoryIdDto.Request request,
		PrincipalDetails principalDetails) {

		Directory deleteDirectory = directoryRepository.findById(request.directoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		Long directoryId = deleteDirectory.getId();
		Integer orderIndex = deleteDirectory.getOrderIndex();
		Directory parentDirectory = deleteDirectory.getParentDirectory();

		directoryRepository.decrementDirectoryAndSiteOrderIndexes(parentDirectory, orderIndex);
		directoryRepository.delete(deleteDirectory);

		return directoryId;
	}

	@Transactional
	@ValidationApplied
	public Long changeParentDirectory(DirectoryChangeParentDto.Request request,
		PrincipalDetails principalDetails) {

		Directory movingDirectory = directoryRepository.findById(request.movingDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		Directory newParentDirectory = directoryRepository.findById(request.newParentDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		directoryRepository.decrementDirectoryAndSiteOrderIndexes(
			movingDirectory.getParentDirectory(),
			movingDirectory.getOrderIndex()
		);

		movingDirectory.setParentDirectory(newParentDirectory);
		movingDirectory.setOrderIndex(newParentDirectory.getNextOrderIndex());

		return movingDirectory.getId();
	}

	@Transactional
	@ValidationApplied
	public DirectoryDragAndDropDto.Response dragAndDropDirectoryOrSite(
		DirectoryDragAndDropDto.Request request,
		PrincipalDetails principalDetails) {

		Integer currentItemOrderIndex;

		Directory parentDirectory = directoryRepository.findById(request.parentDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		currentItemOrderIndex = getOrderIndex(
			request.targetId(),
			request.itemType()
		);

		boolean isIncrement = currentItemOrderIndex > request.targetOrderIndex();
		int startIndex = Math.min(currentItemOrderIndex, request.targetOrderIndex());
		int endIndex = Math.max(currentItemOrderIndex, request.targetOrderIndex());

		directoryRepository.updateDirectoryAndSiteOrderIndexesInRange(parentDirectory, startIndex, endIndex,
			isIncrement);

		setOrderIndex(
			request.targetId(),
			request.itemType(),
			request.targetOrderIndex()
		);

		return DirectoryDragAndDropDto.Response.builder()
			.targetId(request.targetId())
			.itemType(String.valueOf(request.itemType()))
			.targetOrderIndex(request.targetOrderIndex())
			.build();
	}

	private Integer getOrderIndex(Long targetId, ItemType itemType) {
		switch (itemType) {
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

	private void setOrderIndex(Long targetId, ItemType itemType,
		Integer targetOrderIndex) {
		switch (itemType) {
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
	public DirectoryIdDto.Response findDirectoryDetails(
		DirectoryIdDto.Request request,
		PrincipalDetails principalDetails) {

		Directory targetDirectory = directoryRepository.findById(request.directoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		List<Favorite> favorites = favoriteRepository.findByMember(principalDetails.getMember());

		List<Long> favoriteDirectoryIds = favoriteService.findFavoriteDirectoryIds(favorites);
		List<Long> favoriteSiteIds = favoriteService.findFavoriteSiteIds(favorites);

		List<DirectoryDetailResponse> directoryDetailResponses =
			directoryRepository.findDirectoryDetails(targetDirectory.getId(), favoriteDirectoryIds);

		List<SiteDetailResponse> siteDetailResponses =
			siteRepository.findSitesDetails(targetDirectory.getId(), favoriteSiteIds);

		return DirectoryIdDto.Response.builder()
			.targetDirectoryDescription(targetDirectory.getDirectoryDescription())
			.targetDirectoryName(targetDirectory.getDirectoryName())
			.directoryDetailResponses(directoryDetailResponses)
			.siteDetailResponses(siteDetailResponses)
			.build();
	}

	@Transactional
	@ValidationApplied
	public DirectoryPasteDto.Response pasteDirectory(
		DirectoryPasteDto.Request request,
		PrincipalDetails principalDetails) {

		Directory originalDirectory = directoryRepository.findById(request.originalDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		Directory destinationDirectory = directoryRepository.findById(request.destinationDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		directoryRepository.incrementDirectoryAndSiteOrderIndexes(destinationDirectory, 0);

		Directory pastedDirectory = originalDirectory.cloneDirectory(destinationDirectory);
		pastedDirectory.setOrderIndex(1);
		directoryRepository.save(pastedDirectory);

		return DirectoryPasteDto.Response.builder()
			.pastedirectoryId(pastedDirectory.getId())
			.destinationDirectoryId(destinationDirectory.getId())
			.clonedDirectoryName(pastedDirectory.getDirectoryName())
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
