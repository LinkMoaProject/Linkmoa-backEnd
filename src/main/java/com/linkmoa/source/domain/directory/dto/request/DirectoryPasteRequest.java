package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

public record DirectoryPasteRequest(

	BaseRequest baseRequest,
	Long originalDirectoryId,
	Long destinationDirectoryId
) {
}
