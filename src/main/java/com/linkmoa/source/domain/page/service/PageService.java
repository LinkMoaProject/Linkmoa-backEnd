package com.linkmoa.source.domain.page.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.repository.MemberRepository;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.domain.notify.aop.annotation.NotifyApplied;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.dto.request.PageCreateRequest;
import com.linkmoa.source.domain.page.dto.request.PageDeleteRequest;
import com.linkmoa.source.domain.page.dto.request.PageInvitationRequestCreate;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.dto.response.PageInvitationRequestCreateResponse;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.entity.PageInvitationRequest;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.page.repository.PageInviteRequestRepository;
import com.linkmoa.source.domain.page.repository.PageRepository;
import com.linkmoa.source.global.aop.annotation.ValidationApplied;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final MemberService memberService;
    private final DirectoryRepository directoryRepository;
    private final MemberPageLinkRepository memberPageLinkRepository;
    private final PageInviteRequestRepository pageInviteRequestRepository;


    @Transactional
    public ApiPageResponseSpec<Long> createPage(PageCreateRequest requestDto, PrincipalDetails principalDetails) {

        Member hostMember = memberService.findMemberByEmail(principalDetails.getEmail());

        Directory rootDirectory = createRootDirectory(hostMember);
        Page newPage = createNewPage(requestDto,rootDirectory);
        MemberPageLink memberPageLink = createMemberPageLink(hostMember, newPage);
        saveEntities(newPage, memberPageLink, rootDirectory);

        return ApiPageResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Page 생성에 성공했습니다.")
                .data(newPage.getId())
                .build();
    }

    private Page createNewPage(PageCreateRequest requestDto, Directory rootDirectory) {
        return Page.builder()
                .pageType(requestDto.pageType())
                .pageTitle(requestDto.pageTitle())
                .pageDescription(requestDto.pageDescription())
                .rootDirectory(rootDirectory)
                .build();
    }

    private Directory createRootDirectory(Member hostMember) {
        return Directory.builder()
                .directoryName(hostMember.getEmail() + "님의 Root directory name")
                .directoryDescription(hostMember.getEmail() + "님의 Root directory description")
                .build();
    }

    private MemberPageLink createMemberPageLink(Member hostMember, Page page) {
        return MemberPageLink.builder()
                .member(hostMember)
                .page(page)
                .permissionType(PermissionType.HOST)
                .build();
    }

    @Transactional
    public void saveEntities(Page page, MemberPageLink memberPageLink, Directory rootDirectory) {
        pageRepository.save(page);
        memberPageLinkRepository.save(memberPageLink);
        directoryRepository.save(rootDirectory);
    }


    @Transactional
    public ApiPageResponseSpec<Long> deletePage(PageDeleteRequest pageDeleteRequest, PrincipalDetails principalDetails) {
        pageRepository.deleteById(pageDeleteRequest.baseRequest().pageId());
        return ApiPageResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Page 삭제에 성공했습니다.")
                .data(pageDeleteRequest.baseRequest().pageId())
                .build();
    }
    @Transactional
    @ValidationApplied
    @NotifyApplied
    public PageInvitationRequest createPageInviteRequest(PageInvitationRequestCreate pageInvitationRequestCreate, PrincipalDetails principalDetails){

        if (!memberService.isMemberExist(pageInvitationRequestCreate.receiverEmail())) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL); // 유저가 없으면 예외 발생
        }

        Page page = pageRepository.findById(pageInvitationRequestCreate.baseRequest().pageId())
                .orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));

        if (page.getPageType() == PageType.PERSONAL) {
            throw new PageException(PageErrorCode.CANNOT_INVITE_TO_PERSONAL_PAGE);
        }

        PageInvitationRequest pageInvitationRequest = PageInvitationRequest.builder()
                .senderEmail(principalDetails.getEmail())
                .receiverEmail(pageInvitationRequestCreate.receiverEmail())
                .page(page)
                .build();

        return pageInviteRequestRepository.save(pageInvitationRequest);
    }

    public ApiPageResponseSpec<PageInvitationRequestCreateResponse> mapToPageInviteRequestResponse(PageInvitationRequest pageInvitationRequest){

        PageInvitationRequestCreateResponse pageInvitationRequestCreateResponse = PageInvitationRequestCreateResponse.builder()
                .pageTitle(pageInvitationRequest.getPage().getPageTitle())
                .receiverEmail(pageInvitationRequest.getSenderEmail())
                .senderEmail(pageInvitationRequest.getSenderEmail())
                .build();

        return ApiPageResponseSpec.<PageInvitationRequestCreateResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("공유 page 초대를 보냈습니다.")
                .data(pageInvitationRequestCreateResponse)
                .build();
    }







}
