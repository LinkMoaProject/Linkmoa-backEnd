package com.linkmoa.source.domain.page.controller.impl;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.page.controller.spec.PageApiSpecification;
import com.linkmoa.source.domain.page.dto.request.PageCreateRequest;
import com.linkmoa.source.domain.page.dto.request.PageDeleteRequest;
import com.linkmoa.source.domain.page.dto.request.PageInvitationRequestCreate;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.dto.response.PageInvitationRequestCreateResponse;
import com.linkmoa.source.domain.page.entity.PageInvitationRequest;
import com.linkmoa.source.domain.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiPageResponseSpec<PageInvitationRequestCreateResponse>> invitePage(
            PageInvitationRequestCreate pageInvitationRequest,
            PrincipalDetails principalDetails) {
        ApiPageResponseSpec<PageInvitationRequestCreateResponse> pageInviteRequestResponse =
                pageService.mapToPageInviteRequestResponse(pageService.createPageInviteRequest(pageInvitationRequest, principalDetails));

        return ResponseEntity.ok().body(pageInviteRequestResponse);
    }
}
