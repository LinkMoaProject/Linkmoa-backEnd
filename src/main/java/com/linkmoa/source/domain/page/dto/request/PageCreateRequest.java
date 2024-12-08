package com.linkmoa.source.domain.page.dto.request;

import com.linkmoa.source.domain.page.contant.PageType;

import com.linkmoa.source.global.command.constant.CommandType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PageCreateRequest(
        @Size(max=20) String pageTitle,
        @Size(max=100)String pageDescription,
        @NotNull PageType pageType,
        CommandType commandType
) {
}
