package com.linkmoa.source.domain.page.dto.request;

import com.linkmoa.source.domain.page.contant.PageType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public class PageCreateDto {
	@Builder
	public record Request(
		@Size(max = 50) String pageTitle,
		@Size(max = 100) String pageDescription,
		@NotNull PageType pageType
	) {
	}
}
