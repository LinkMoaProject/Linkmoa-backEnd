package com.linkmoa.source.domain.page.controller.impl;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.page.controller.spec.PageApiSpecification;
import com.linkmoa.source.domain.page.dto.request.PageCreateRequestDto;
import com.linkmoa.source.domain.page.dto.request.PageDeleteRequestDto;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pages")
public class PageApiController implements PageApiSpecification {

    private final PageService pageService;


    public ResponseEntity<ApiPageResponseSpec<Long>> createPage(
            PageCreateRequestDto pageCreateRequestDto,
            PrincipalDetails principalDetails) {
        ApiPageResponseSpec<Long> createPageResponse = pageService.createPage(pageCreateRequestDto, principalDetails);

        return ResponseEntity.ok().body(createPageResponse);
    }

    public ResponseEntity<ApiPageResponseSpec<Long>> deletePage(
            PageDeleteRequestDto pageDeleteRequestDto,
            PrincipalDetails principalDetails) {
        ApiPageResponseSpec<Long> deletePageResponse = pageService.deletePage(pageDeleteRequestDto, principalDetails);

        return ResponseEntity.ok().body(deletePageResponse);
    }
}
