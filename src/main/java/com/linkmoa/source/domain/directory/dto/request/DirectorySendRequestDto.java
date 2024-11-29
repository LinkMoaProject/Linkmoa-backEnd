package com.linkmoa.source.domain.directory.dto.request;


import com.linkmoa.source.global.dto.request.BaseRequestDto;
import lombok.Builder;

@Builder
public record DirectorySendRequestDto(

        BaseRequestDto baseRequestDto,
        String receiverEmail,
        Long directoryId
) {
}
