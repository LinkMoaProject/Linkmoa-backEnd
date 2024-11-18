package com.linkmoa.source.global.dto.request;

import com.linkmoa.source.global.command.constant.CommandType;

public record BaseRequestDto(
        Long memberId,
        Long pageId,
        CommandType commandType
) {
}
