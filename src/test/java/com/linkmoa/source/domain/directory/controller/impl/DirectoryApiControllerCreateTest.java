package com.linkmoa.source.domain.directory.controller.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkmoa.source.auth.annotation.WithMockCustomUser;

import com.linkmoa.source.auth.jwt.filter.JwtAuthorizationFilter;
import com.linkmoa.source.auth.jwt.provider.JwtTokenProvider;
import com.linkmoa.source.auth.jwt.service.JwtService;
import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.auth.security.WithMockUserSecurityContextFactory;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequestDto;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.directory.service.DirectoryService;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.notify.service.DirectorySendRequestService;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.repository.PageRepository;
import com.linkmoa.source.global.aop.aspect.ValidationAspect;
import com.linkmoa.source.global.command.constant.CommandType;
import com.linkmoa.source.global.command.service.CommandService;
import com.linkmoa.source.global.dto.request.BaseRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest(DirectoryApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(ValidationAspect.class) // AOP를 활성화
class DirectoryApiControllerTest {

    @MockBean
    private JwtService jwtService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private DirectoryService directoryService;

    @MockBean
    private DirectorySendRequestService directorySendRequestService; // MockBean으로 등록

    @MockBean
    private JwtAuthorizationFilter jwtAuthorizationFilter; // 필터를 Mock 처리

    @MockBean
    private PageRepository pageRepository;

    @MockBean
    private CommandService commandService;

    @MockBean
    private DirectoryRepository directoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockCustomUser(userEmail = "test@google.com", userRole = "ROLE_USER")
    void testSecurityContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
    }

    @Test
    @WithMockCustomUser(userEmail = "test@google.com", userRole = "ROLE_USER")
    void createDirectory_Success() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        Member member = principalDetails.getMember();


        // Given
        BaseRequestDto baseRequestDto = new BaseRequestDto(
                1L, // pageId
                CommandType.CREATE // commandType
        );

        DirectoryCreateRequestDto requestDto = new DirectoryCreateRequestDto(
                baseRequestDto,              // BaseRequestDto 객체
                "Test Directory",            // directoryName
                null,                          // parentDirectoryId
                "This is a test directory"   // directoryDescription
        );

        Page page = Page.builder()
                .pageTitle("Sample Page")
                .pageDescription("Sample Description")
                .pageType(PageType.PERSONAL)
                .build();

        when(memberService.findMemberByEmail("test@google.com")).thenReturn(member);

        // Mock 설정: MemberPageLinkRepository
        when(commandService.getUserPermissionType(member.getId(), 1L))
                .thenReturn(PermissionType.HOST);

        // Member와 Page 간의 연결
        MemberPageLink memberPageLink = MemberPageLink.builder()
                .member(member)
                .page(page)
                .permissionType(PermissionType.HOST) // 권한 설정
                .build();

        when(pageRepository.findById(1L)).thenReturn(Optional.of(page));

        // Given: 응답 값 미리 생성
        ApiDirectoryResponseSpec<Long> mockResponse = new ApiDirectoryResponseSpec<>(
                HttpStatus.OK,
                "Directory 생성에 성공했습니다.", // message
                1L // data
        );

        given(directoryService.createDirectory(
                Mockito.eq(requestDto), // 정확한 객체 비교
                Mockito.any(PrincipalDetails.class)
        )).willReturn(mockResponse);


        //given(directoryService.createDirectory(any(),any())).willReturn(mockResponse);

        // JSON 직렬화 확인
        String jsonContent = objectMapper.writeValueAsString(requestDto);
        log.info("Generated JSON: {}", jsonContent);


        String mockAccessToken = WithMockUserSecurityContextFactory.getAccessToken();


        mockMvc.perform(
                post("/api/directories")   // POST 요청
                        .header("access_token", mockAccessToken)  // 인증 헤더 추가
                        .with(csrf())                                        // CSRF 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)           // JSON 요청 타입 설정
                        .content(jsonContent)                              // 요청 본문 추가
                )
                .andExpect(status().isOk())                              // HTTP 상태 200 검증
                .andExpect((ResultMatcher) jsonPath("$.data").value(1L))               // 응답 JSON의 data 필드가 1L인지 검증
                .andExpect((ResultMatcher) jsonPath("$.message").value("Directory 생성에 성공했습니다.")); // 메시지 검증

        // Then: Mock 호출 검증
        verify(directoryService).createDirectory(
                Mockito.eq(requestDto), // 호출된 객체 확인
                Mockito.any(PrincipalDetails.class)
        );
    }




}


