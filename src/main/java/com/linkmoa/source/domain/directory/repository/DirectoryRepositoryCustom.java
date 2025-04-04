package com.linkmoa.source.domain.directory.repository;

import java.util.List;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectorySimpleResponse;
import com.linkmoa.source.domain.directory.entity.Directory;

public interface DirectoryRepositoryCustom {
	List<DirectoryDetailResponse> findDirectoryDetails(Long directoryId, List<Long> favoriteDirectoryIds);

	void decrementDirectoryOrderIndexes(Directory parentDirectory, Integer orderIndex);

	void decrementSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);

	void decrementDirectoryAndSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);

	void incrementDirectoryOrderIndexes(Directory parentDirectory, Integer orderIndex);

	void incrementSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);

	void incrementDirectoryAndSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);

	void updateDirectoryOrderIndexesInRange(Directory parentDirectory, Integer startIndex, Integer endIndex,
		Integer adjustmentValue);

	void updateSiteOrderIndexesInRange(Directory parentDirectory, Integer startIndex, Integer endIndex,
		Integer adjustmentValue);

	void updateDirectoryAndSiteOrderIndexesInRange(Directory parentDirectory, Integer startIndex, Integer endIndex,
		boolean isIncrement);

	List<DirectorySimpleResponse> findFavoriteDirectories(List<Long> favoriteDirectoryIds);

}
