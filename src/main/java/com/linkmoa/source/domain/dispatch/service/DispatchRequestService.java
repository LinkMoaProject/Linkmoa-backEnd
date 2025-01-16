package com.linkmoa.source.domain.dispatch.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestCreate;
import com.linkmoa.source.domain.dispatch.dto.response.*;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.dispatch.dto.request.DirectoryTransmissionSendRequest;
import com.linkmoa.source.domain.dispatch.entity.DirectoryTransmissionRequest;
import com.linkmoa.source.domain.dispatch.entity.SharePageInvitationRequest;
import com.linkmoa.source.domain.dispatch.repository.DirectoryTransmissionRequestRepository;
import com.linkmoa.source.domain.dispatch.repository.SharePageInvitationRequestRepository;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.notification.aop.annotation.NotificationApplied;
import com.linkmoa.source.domain.notification.repository.NotificationRepository;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.page.repository.PageRepository;
import com.linkmoa.source.global.aop.annotation.ValidationApplied;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DispatchRequestService {
 
    private final MemberService memberService;
    private final DirectoryRepository directoryRepository;
    private final DirectoryTransmissionRequestRepository directoryTransmissionRequestRepository;
    private final PageRepository pageRepository;
    private final SharePageInvitationRequestRepository sharePageInvitationRequestRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    @ValidationApplied
    @NotificationApplied
    public DirectoryTransmissionRequest createDirectoryTransmissionRequest(DirectoryTransmissionSendRequest directoryTransmissionSendRequest, PrincipalDetails principalDetails) {

        if (!memberService.isMemberExist(directoryTransmissionSendRequest.receiverEmail())) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL);
        }

        Directory directory = directoryRepository.findById(directoryTransmissionSendRequest.directoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        DirectoryTransmissionRequest directoryTransmissionRequest = DirectoryTransmissionRequest.builder()
                .senderEmail(principalDetails.getEmail())
                .receiverEmail(directoryTransmissionSendRequest.receiverEmail())
                .directory(directory)
                .build();

        return directoryTransmissionRequestRepository.save(directoryTransmissionRequest);
    }

    public ApiDirectoryResponseSpec<DirectorySendResponse> mapToDirectorySendResponse(DirectoryTransmissionRequest directoryTransmissionRequest)
    {
        DirectorySendResponse directorySendResponse = DirectorySendResponse.builder()
                .directoryName(directoryTransmissionRequest.getDirectory().getDirectoryName())
                .receiverEmail(directoryTransmissionRequest.getReceiverEmail())
                .senderEmail(directoryTransmissionRequest.getSenderEmail())
                .build();

        return ApiDirectoryResponseSpec.<DirectorySendResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("디렉토리 전송 요청을 보냈습니다.")
                .data(directorySendResponse)
                .build();
    }


    @Transactional
    @ValidationApplied
    @NotificationApplied
    public SharePageInvitationRequest createSharePageInviteRequest(SharePageInvitationRequestCreate sharePageInvitationRequestCreate, PrincipalDetails principalDetails){

        if (!memberService.isMemberExist(sharePageInvitationRequestCreate.receiverEmail())) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL); // 유저가 없으면 예외 발생
        }

        Page page = pageRepository.findById(sharePageInvitationRequestCreate.baseRequest().pageId())
                .orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));

        if (page.getPageType() == PageType.PERSONAL) {
            throw new PageException(PageErrorCode.CANNOT_INVITE_TO_PERSONAL_PAGE);
        }

        SharePageInvitationRequest sharePageInvitationRequest = SharePageInvitationRequest.builder()
                .senderEmail(principalDetails.getEmail())
                .receiverEmail(sharePageInvitationRequestCreate.receiverEmail())
                .page(page)
                .permissionType(sharePageInvitationRequestCreate.permissionType())
                .build();
        return sharePageInvitationRequestRepository.save(sharePageInvitationRequest);
    }
    public ApiPageResponseSpec<SharePageInvitationRequestCreateResponse> mapToPageInviteRequestResponse(SharePageInvitationRequest sharePageInvitationRequest){

        SharePageInvitationRequestCreateResponse sharePageInvitationRequestCreateResponse = SharePageInvitationRequestCreateResponse.builder()
                .pageTitle(sharePageInvitationRequest.getPage().getPageTitle())
                .receiverEmail(sharePageInvitationRequest.getReceiverEmail())
                .senderEmail(sharePageInvitationRequest.getSenderEmail())
                .PageInvitationRequestId(sharePageInvitationRequest.getId())
                .build();

        return ApiPageResponseSpec.<SharePageInvitationRequestCreateResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("공유 페이지 초대를 보냈습니다.")
                .data(sharePageInvitationRequestCreateResponse)
                .build();
    }

    public List<DispatchDetailResponse> findSharePageInvitationsForReceiver(String receiverEmail){
        List<DispatchDetailResponse> allSharePageInvitationsByReceiverEmail =
                sharePageInvitationRequestRepository.findAllSharePageInvitationsByReceiverEmail(receiverEmail);

        return allSharePageInvitationsByReceiverEmail;
    }

    public List<DispatchDetailResponse> findDirectoryDirectoryTransmissionsForReceiver(String receiverEmail){
        List<DispatchDetailResponse> allDirectoryTransmissionRequestByReceiverEmail =
                directoryTransmissionRequestRepository.findAllDirectoryTransmissionRequestByReceiverEmail(receiverEmail);

        return allDirectoryTransmissionRequestByReceiverEmail;
    }

    @Transactional // 이 메서드가 트랜잭션 내에서 실행되도록 보장
    public ApiDispatchResponseSpec<NotificationsDetailsResponse> findAllNotificationsForReceiver(String receiverEmail){
        NotificationsDetailsResponse notificationDetails = NotificationsDetailsResponse.builder()
                .DirectoryTransmissionRequests(findDirectoryDirectoryTransmissionsForReceiver(receiverEmail))
                .SharePageInvitationRequests(findSharePageInvitationsForReceiver(receiverEmail))
                .build();

        notificationRepository.updateUnreadNotificationsToReadByReceiverEmail(receiverEmail);

        return ApiDispatchResponseSpec.<NotificationsDetailsResponse>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("알람 목록을 조회했습니다.")
                .data(notificationDetails)
                .build();

    }






}
