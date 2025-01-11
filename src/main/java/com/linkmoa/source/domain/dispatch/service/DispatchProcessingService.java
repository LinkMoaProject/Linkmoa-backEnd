/*
package com.linkmoa.source.domain.dispatch.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.dispatch.dto.request.DispatchProcessingRequest;
import com.linkmoa.source.domain.dispatch.dto.response.ApiDispatchResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.response.SharePageInvitationActionResponse;
import com.linkmoa.source.domain.dispatch.service.processor.SharePageInvitationRequestProcessor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DispatchProcessingService {

    private final SharePageInvitationRequestProcessor sharePageInvitationRequestProcessor;

    public ApiDispatchResponseSpec<SharePageInvitationActionResponse> processSharePageInvitation(
            DispatchProcessingRequest dispatchProcessingRequest,
            PrincipalDetails principalDetails) {

        // SharePageInvitationRequestProcessor의 메서드 호출
        return sharePageInvitationRequestProcessor.processRequest(dispatchProcessingRequest, principalDetails);
    }

}
*/
