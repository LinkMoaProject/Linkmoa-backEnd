package com.linkmoa.source.domain.site.service;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.entity.Role;
import com.linkmoa.source.domain.site.dto.request.SiteCreateRequestDto;
import com.linkmoa.source.domain.site.dto.response.ApiSiteResponse;
import com.linkmoa.source.domain.site.entity.Site;
import com.linkmoa.source.domain.site.repository.SiteRepository;
import com.linkmoa.source.global.aop.LogAspect;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@Transactional
@ExtendWith(MockitoExtension.class) // JUnit 5에서 Mockito 사용을 위한 어노테이션
@Import(LogAspect.class)
public class SiteServiceTest {

    @Mock
    private SiteService siteService;
    @Mock
    private SiteRepository siteRepository;

    @Mock
    private DirectoryRepository directoryRepository;


    @Test
    @WithMockUser
    void saveSite() {

        // Setup
        SiteCreateRequestDto requestDto = new SiteCreateRequestDto("testSite", "http://testsite.com", null);

        Member member = Member.builder()
                .email("eamil")
                .password("password")
                .role(Role.valueOf("ROLE_USER"))
                .nickname("nickname")
                .provider("provide")
                .providerId("providerId")
                .build();

        PrincipalDetails principalDetails = new PrincipalDetails(member);



        // Mocking the repository call
        Site site = Site.builder()
                .siteName(requestDto.siteName())
                .siteUrl(requestDto.siteUrl())
                .memberId(principalDetails.getId())
                .build();

        //Mockito.when(siteRepository.save(Mockito.any(Site.class))).thenReturn(site);
        Mockito.when(siteService.saveSite(Mockito.any(SiteCreateRequestDto.class), Mockito.any(PrincipalDetails.class)))
                .thenReturn(new ApiSiteResponse<Long>(HttpStatus.OK,
                        "site 생성 및 저장에 성공하였습니다.",
                        site.getId()));



        // Service call
        ApiSiteResponse<Long> response = siteService.saveSite(requestDto, principalDetails);

        // Log values for debugging
        log.info("Site Name: {}", requestDto.siteName());
        log.info("Site URL: {}", requestDto.siteUrl());
        log.info("Site ID: {}", site.getId());
        log.info("Member Email: {}", member.getEmail());
        log.info("Member Nickname: {}", member.getNickname());

        // Assertions
        //assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals("site 생성 및 저장에 성공하였습니다.", response.getSuccessMessage());
        assertEquals(site.getId(), response.getData());

    }

}