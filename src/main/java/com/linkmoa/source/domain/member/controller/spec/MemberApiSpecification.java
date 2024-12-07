package com.linkmoa.source.domain.member.controller.spec;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.member.dto.request.MemberSignUpRequest;
import com.linkmoa.source.global.spec.ApiResponseSpec;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MemberApiSpecification {

    @Tag(name = "Post", description = "회원 관련 API")
    @Operation(summary = "회원가입 추가 정보 등록", description = "회원가입을 통해 추가 정보를 등록합니다.")
    @ApiErrorCodeExamples(DirectoryErrorCode.class)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<ApiResponseSpec> memberSignUp(
            @RequestBody @Validated MemberSignUpRequest memberSignUpRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );
}
