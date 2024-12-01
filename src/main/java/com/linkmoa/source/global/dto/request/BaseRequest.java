package com.linkmoa.source.global.dto.request;

import com.linkmoa.source.global.constant.CommandType;
import jakarta.validation.constraints.NotNull;

public record BaseRequest(
        @NotNull Long pageId,
        CommandType commandType
) {
}
