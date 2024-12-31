package com.linkmoa.source.domain.page.controller.impl;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.page.controller.spec.PageApiSpecification;
import com.linkmoa.source.domain.page.dto.request.PageCreateRequest;
import com.linkmoa.source.domain.page.dto.request.PageDeleteRequest;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.dto.response.PageDetailsResponse;
import com.linkmoa.source.domain.page.dto.response.PageResponse;
import com.linkmoa.source.domain.page.dto.response.SharePageLeaveResponse;
import com.linkmoa.source.domain.page.service.PageService;
import com.linkmoa.source.global.dto.request.BaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/page")
public class PageApiController implements PageApiSpecification {

    private final PageService pageService;


    public ResponseEntity<ApiPageResponseSpec<Long>> createPage(
            PageCreateRequest pageCreateRequest,
            PrincipalDetails principalDetails) {
        ApiPageResponseSpec<Long> createPageResponse = pageService.createPage(pageCreateRequest, principalDetails);

        return ResponseEntity.ok().body(createPageResponse);
    }

    public ResponseEntity<ApiPageResponseSpec<Long>> deletePage(
            PageDeleteRequest pageDeleteRequest,
            PrincipalDetails principalDetails) {
        ApiPageResponseSpec<Long> deletePageResponse = pageService.deletePage(pageDeleteRequest, principalDetails);

        return ResponseEntity.ok().body(deletePageResponse);
    }


    public ResponseEntity<ApiPageResponseSpec<List<PageResponse>>> getAllPages(
            PrincipalDetails principalDetails) {

        ApiPageResponseSpec<List<PageResponse>> allPages = pageService.findAllPages(principalDetails);

        return ResponseEntity.ok().body(allPages);

    }


    public ResponseEntity<ApiPageResponseSpec<SharePageLeaveResponse>> leaveSharePage(
            BaseRequest baseRequest,
            PrincipalDetails principalDetails) {
        ApiPageResponseSpec<SharePageLeaveResponse> sharePageLeaveResponse= pageService.leaveSharePage(baseRequest, principalDetails);

        return ResponseEntity.ok().body(sharePageLeaveResponse);
    }

    public ResponseEntity<ApiPageResponseSpec<PageDetailsResponse>> getPageDetails(
            BaseRequest baseRequest,
            PrincipalDetails principalDetails) {
        ApiPageResponseSpec<PageDetailsResponse> pageDetailsResponse = pageService.findPageMain(baseRequest, principalDetails);
        return ResponseEntity.ok().body(pageDetailsResponse);
    }
}
