package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DirectoryCreateDto {

	public record Request(
		BaseRequest baseRequest,
		@NotBlank @Size(max = 80) String directoryName,
		Long parentDirectoryId,
		@Size(max = 300) String directoryDescription
	) {

	}
}
