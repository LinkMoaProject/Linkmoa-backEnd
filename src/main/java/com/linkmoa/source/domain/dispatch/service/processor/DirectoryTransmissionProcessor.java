package com.linkmoa.source.domain.dispatch.service.processor;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.dispatch.dto.request.DispatchProcessingRequest;
import com.linkmoa.source.domain.dispatch.dto.response.ApiDispatchResponseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DirectoryTransmissionProcessor implements DispatchProcessor{

    @Override
    @Transactional
    public ApiDispatchResponseSpec<?> processRequest(DispatchProcessingRequest dispatchProcessingRequest, PrincipalDetails principalDetails) {
        return null;
    }
}
