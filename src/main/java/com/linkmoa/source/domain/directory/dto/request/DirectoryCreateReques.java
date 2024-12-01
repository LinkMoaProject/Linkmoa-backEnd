package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record DirectoryCreateReques(

        BaseRequest baseRequest,
        @NotBlank @Size(max=20) String directoryName,
        Long parentDirectoryId,
        @Size(max=100) String directoryDescription
) {
}
