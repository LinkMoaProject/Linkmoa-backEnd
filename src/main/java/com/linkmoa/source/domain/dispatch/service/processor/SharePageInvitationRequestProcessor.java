package com.linkmoa.source.domain.dispatch.service.processor;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.dto.response.ApiDispatchResponseSpec;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.domain.page.repository.PageRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SharePageInvitationRequestProcessor implements DispatchProcessor {

    private final PageRepository pageRepository;
    private final MemberPageLinkRepository memberPageLinkRepository;
    @Override
    public ApiDispatchResponseSpec<?> processRequest(Long requestId, RequestStatus status, PrincipalDetails principalDetails) {

        return null;
    }
}
