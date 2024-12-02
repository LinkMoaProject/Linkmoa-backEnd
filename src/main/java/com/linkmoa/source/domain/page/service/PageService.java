package com.linkmoa.source.domain.page.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.domain.notify.aop.annotation.NotifyApplied;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.dto.request.PageCreateRequest;
import com.linkmoa.source.domain.page.dto.request.PageDeleteRequest;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationActionRequest;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestCreate;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.response.SharePageInvitationActionResponse;
import com.linkmoa.source.domain.dispatch.dto.response.SharePageInvitationRequestCreateResponse;
import com.linkmoa.source.domain.page.dto.response.SharePageLeaveResponse;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.dispatch.entity.SharePageInvitationRequest;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.dispatch.repository.SharePageInviteRequestRepository;
import com.linkmoa.source.domain.page.repository.PageRepository;
import com.linkmoa.source.global.aop.annotation.ValidationApplied;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.global.dto.request.BaseRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final MemberService memberService;
    private final DirectoryRepository directoryRepository;
    private final MemberPageLinkRepository memberPageLinkRepository;
    private final SharePageInviteRequestRepository sharePageInviteRequestRepository;


    @Transactional
    public ApiPageResponseSpec<Long> createPage(PageCreateRequest requestDto, PrincipalDetails principalDetails) {

        Member hostMember = memberService.findMemberByEmail(principalDetails.getEmail());

        Directory rootDirectory = createRootDirectory(hostMember);
        Page newPage = createNewPage(requestDto,rootDirectory);
        MemberPageLink memberPageLink = createMemberPageLink(hostMember, newPage);
        saveEntities(newPage, memberPageLink, rootDirectory);

        return ApiPageResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage(newPage.getPageType().toString()+"페이지 생성에 성공했습니다.")
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
    @ValidationApplied
    public ApiPageResponseSpec<Long> deletePage(PageDeleteRequest pageDeleteRequest, PrincipalDetails principalDetails) {
        pageRepository.deleteById(pageDeleteRequest.baseRequest().pageId());
        return ApiPageResponseSpec.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("페이지 삭제에 성공했습니다.")
                .data(pageDeleteRequest.baseRequest().pageId())
                .build();
    }




    @Transactional
    @ValidationApplied
    public ApiPageResponseSpec<SharePageLeaveResponse> leaveSharePage(BaseRequest baseRequest, PrincipalDetails principalDetails){

        Page page = pageRepository.findById(baseRequest.pageId()).orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));
        Member member = memberService.findMemberByEmail(principalDetails.getEmail());

        if(page.getPageType()==PageType.PERSONAL){
            throw new PageException(PageErrorCode.CANNOT_LEAVE_PERSONAL_PAGE);
        }

        memberPageLinkRepository.deleteByMemberIdAndPageId(member.getId(),page.getId());

        SharePageLeaveResponse sharePageLeaveResponse = SharePageLeaveResponse.builder()
                .pageId(page.getId())
                .pageTitle(page.getPageTitle())
                .build();

        return ApiPageResponseSpec.<SharePageLeaveResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("공유 페이지 탈퇴에 성공했습니다.")
                .data(sharePageLeaveResponse)
                .build();

    }

    @Transactional
    public ApiPageResponseSpec<SharePageInvitationActionResponse> actionSharePageInvitaion(
            SharePageInvitationActionRequest sharePageInvitationActionRequest,
            PrincipalDetails principalDetails){

        SharePageInvitationRequest sharePageInvitationRequest = sharePageInviteRequestRepository.
                findById(sharePageInvitationActionRequest.sharePageInvitationRequestId())
                .orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));


        if(sharePageInvitationRequest.getRequestStatus() == RequestStatus.REJECTED){
            throw new PageException(PageErrorCode.CANNOT_ACCEPT_ALREADY_REJECTED_INVITATION);
        }
        else if (sharePageInvitationRequest.getRequestStatus() == RequestStatus.ACCEPTED){
            throw new PageException(PageErrorCode.CANNOT_ACCEPT_ALREADY_ACCEPTED_INVITATION);
        }

        Page page =sharePageInvitationRequest.getPage();
        Member member = memberService.findMemberByEmail(principalDetails.getEmail());
        PermissionType permissionType = sharePageInvitationActionRequest.permissionType();

        sharePageInvitationRequest.changeRequestStatus(sharePageInvitationActionRequest.requestStatus());

        if(sharePageInvitationRequest.getRequestStatus()==RequestStatus.ACCEPTED){
            acceptSharePageInvitaion(page,member,permissionType);

            SharePageInvitationActionResponse sharePageInvitationActionResponse = SharePageInvitationActionResponse.builder()
                    .sharePageInvitationRequestId(sharePageInvitationRequest.getId())
                    .requestStatus(sharePageInvitationRequest.getRequestStatus())
                    .senderEmail(sharePageInvitationRequest.getSenderEmail())
                    .build();

            return ApiPageResponseSpec.<SharePageInvitationActionResponse>builder()
                    .httpStatusCode(HttpStatus.OK)
                    .successMessage("공유 페이지 초대를 수락했습니다.")
                    .data(sharePageInvitationActionResponse)
                    .build();
        }
        else{
            SharePageInvitationActionResponse sharePageInvitationActionResponse = SharePageInvitationActionResponse.builder()
                    .sharePageInvitationRequestId(sharePageInvitationRequest.getId())
                    .requestStatus(sharePageInvitationRequest.getRequestStatus())
                    .senderEmail(sharePageInvitationRequest.getSenderEmail())
                    .build();


            return ApiPageResponseSpec.<SharePageInvitationActionResponse>builder()
                    .httpStatusCode(HttpStatus.OK)
                    .successMessage("공유 페이지 초대를 거절했습니다.")
                    .data(sharePageInvitationActionResponse)
                    .build();
        }



    }

    public void acceptSharePageInvitaion(Page page,Member member,PermissionType permissionType){

        Optional<MemberPageLink> existingLink = memberPageLinkRepository.findByMemberAndPage(member.getId(), page.getId());
        if (existingLink.isPresent()) {
            throw new PageException(PageErrorCode.ALREADY_JOINED_SHARED_PAGE);
        }

        MemberPageLink memberPageLink = MemberPageLink.builder()
                .page(page)
                .member(member)
                .permissionType(permissionType)
                .build();

        memberPageLinkRepository.save(memberPageLink);


    }










}
