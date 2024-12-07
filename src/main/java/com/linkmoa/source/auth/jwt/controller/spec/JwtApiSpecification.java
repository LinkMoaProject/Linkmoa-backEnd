package com.linkmoa.source.auth.jwt.controller.spec;

import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.global.spec.ApiResponseSpec;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public interface JwtApiSpecification {

    @Tag(name = "Get", description = "Jwt 관련 API")
    @Operation(summary = "Refreshtoken으로 Accesstoken 생성", description = "Refreshtoken으로 Accesstoken을 생성합니다.")
    @ApiErrorCodeExamples(DirectoryErrorCode.class)
    @GetMapping
    public ResponseEntity<ApiResponseSpec> getAccessToken(
            @Parameter(description = "리프레시 토큰") String refreshToken
    );

}
