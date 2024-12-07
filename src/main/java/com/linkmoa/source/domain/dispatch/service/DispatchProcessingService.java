package com.linkmoa.source.domain.dispatch.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationActionRequest;
import com.linkmoa.source.domain.dispatch.dto.response.SharePageInvitationActionResponse;
import com.linkmoa.source.domain.dispatch.entity.SharePageInvitationRequest;
import com.linkmoa.source.domain.dispatch.repository.DirectoryTransmissionRequestRepository;
import com.linkmoa.source.domain.dispatch.repository.SharePageInvitationRequestRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.page.repository.PageRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DispatchProcessingService {


    private final MemberService memberService;
    private final DirectoryRepository directoryRepository;
    private final DirectoryTransmissionRequestRepository directoryTransmissionRequestRepository;
    private final PageRepository pageRepository;
    private final SharePageInvitationRequestRepository sharePageInvitationRequestRepository;
    private final MemberPageLinkRepository memberPageLinkRepository;



    @Transactional
    public ApiPageResponseSpec<SharePageInvitationActionResponse> actionSharePageInvitaion(
            SharePageInvitationActionRequest sharePageInvitationActionRequest,
            PrincipalDetails principalDetails){

        SharePageInvitationRequest sharePageInvitationRequest = sharePageInvitationRequestRepository.
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
