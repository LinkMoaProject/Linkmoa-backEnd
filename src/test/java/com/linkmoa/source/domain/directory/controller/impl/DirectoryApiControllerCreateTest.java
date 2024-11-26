package com.linkmoa.source.domain.directory.controller.impl;


import com.linkmoa.source.auth.annotation.WithMockCustomUser;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.service.DirectoryService;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.notify.service.DirectorySendRequestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@WebMvcTest(DirectoryApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
class DirectoryApiControllerTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private DirectoryService directoryService;

    @MockBean
    private DirectorySendRequestService directorySendRequestService; // MockBean으로 등록
    @Test
    @WithMockCustomUser(userEmail = "test@google.com", userRole = "ROLE_USER")
    void testSecurityContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();

        System.out.println("Authenticated Member Email: " + member.getEmail());
        System.out.println("Authenticated Member Role: " + member.getRole());

        assertEquals("test@google.com", member.getEmail());
        assertEquals("ROLE_USER", member.getRole().name());
    }


}


