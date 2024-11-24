package com.linkmoa.source.global.dto.request;

import com.linkmoa.source.global.command.constant.CommandType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BaseRequestDto(
        @NotNull Long pageId,
        CommandType commandType
) {
}
