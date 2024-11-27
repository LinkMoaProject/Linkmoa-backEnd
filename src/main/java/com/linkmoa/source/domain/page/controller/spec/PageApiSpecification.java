package com.linkmoa.source.domain.page.controller.spec;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.page.dto.request.PageCreateRequestDto;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PageApiSpecification {


    @Tag(name = "Post", description = "페이지 관련 API")
    @Operation(summary = "페이지 생성", description = "페이지를 생성합니다.(개인,공유)")
    @ApiErrorCodeExamples(PageErrorCode.class)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiPageResponseSpec<Long>> createPage(
            @RequestBody @Validated PageCreateRequestDto pageCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

}
