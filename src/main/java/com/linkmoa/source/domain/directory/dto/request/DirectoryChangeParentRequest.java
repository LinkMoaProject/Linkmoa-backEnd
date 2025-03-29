package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;

public record DirectoryChangeParentRequest(
	BaseRequest baseRequest,
	@NotNull Long movingDirectoryId,         // 이동시키려는 디렉토리
	@NotNull Long newParentDirectoryId       // 새 부모 디렉토리

) {
}
