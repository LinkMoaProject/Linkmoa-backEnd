package com.linkmoa.source.domain.directory.service;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequestDto;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.constant.Role;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.repository.PageRepository;
import com.linkmoa.source.global.command.constant.CommandType;
import com.linkmoa.source.global.dto.request.BaseRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.linkmoa.source.domain.page.entity.Page;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class DirectoryServiceCreateTest {
    @Mock
    private PageRepository pageRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private DirectoryRepository directoryRepository;

    @InjectMocks
    private DirectoryService directoryService;

    private PrincipalDetails principalDetails;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Member 객체 생성
        Member member = Member.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .nickname("TestUser")
                .provider("google")
                .providerId("google123")
                .build();

        // Member의 id 필드 값 강제 설정
        Field idField = member.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, 1L);

        // PrincipalDetails 생성
        principalDetails = new PrincipalDetails(member);

        // lenient를 사용하여 memberService의 동작 설정을 무시 가능하도록 설정
        lenient().when(memberService.findMemberByEmail("test@example.com")).thenReturn(member);
    }



    @Test
    @DisplayName("root directory에 directory 생성 테스트 ")
    public void testCreateDirectory_Success() {
        // given
        DirectoryCreateRequestDto requestDto = new DirectoryCreateRequestDto(
                new BaseRequestDto(1L, CommandType.CREATE),
                "Test Directory",
                null,
                "Test Description"
        );

        Page page = Page.builder()
                .pageTitle("Sample Page")
                .pageDescription("Sample Description")
                .pageType(PageType.PERSONAL)
                .build();

        Directory directory = Directory.builder()
                .directoryName(requestDto.directoryName())
                .directoryDescription(requestDto.directoryDescription())
                .page(page)
                .parentDirectory(null)
                .build();

        when(pageRepository.findById(1L)).thenReturn(Optional.of(page));
        when(directoryRepository.save(any(Directory.class))).thenAnswer(invocation -> {
            Directory savedDirectory = invocation.getArgument(0);

            // 리플렉션으로 Directory의 id 필드에 값 설정
            Field idField = Directory.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(savedDirectory, 1L);  // ID를 강제로 설정

            return savedDirectory;
        });

        ApiDirectoryResponseSpec<Long> response = directoryService.createDirectory(requestDto, principalDetails);

        // then
        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals("Directory 생성에 성공했습니다.", response.getSuccessMessage());
        assertNotNull(response.getData());

        // 검증
        verify(pageRepository, times(1)).findById(1L);
        verify(directoryRepository, times(1)).save(any(Directory.class));
    }


    @Test
    @DisplayName("parentDirectory가 존재할 때,  directory 생성 테스트 ")
    public void testCreateDirectory_WithParentDirectory() {
        // given
        DirectoryCreateRequestDto requestDto = new DirectoryCreateRequestDto(
                new BaseRequestDto(1L, CommandType.CREATE),
                "Test Sub Directory",
                2L,  // Parent directory ID가 있는 경우
                "Test Sub Description"
        );

        Page page = Page.builder()
                .pageTitle("Sample Page")
                .pageDescription("Sample Description")
                .pageType(PageType.BASIC)
                .build();

        Directory parentDirectory = Directory.builder()
                .directoryName("Parent Directory")
                .directoryDescription("Parent Description")
                .page(page)
                .build();

        Directory directory = Directory.builder()
                .directoryName(requestDto.directoryName())
                .directoryDescription(requestDto.directoryDescription())
                .page(page)
                .parentDirectory(parentDirectory)
                .build();

        // Mock 동작 설정
        when(pageRepository.findById(1L)).thenReturn(Optional.of(page));
        when(directoryRepository.findById(2L)).thenReturn(Optional.of(parentDirectory));
        when(directoryRepository.save(any(Directory.class))).thenAnswer(invocation -> {
            Directory savedDirectory = invocation.getArgument(0);

            // 리플렉션으로 Directory의 id 필드에 값 설정
            Field idField = Directory.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(savedDirectory, 1L);  // ID를 강제로 설정

            return savedDirectory;
        });

        // when
        ApiDirectoryResponseSpec<Long> response = directoryService.createDirectory(requestDto, principalDetails);

        // then
        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals("Directory 생성에 성공했습니다.", response.getSuccessMessage());
        assertNotNull(response.getData());

        // 검증
        verify(pageRepository, times(1)).findById(1L);
        verify(directoryRepository, times(1)).findById(2L);
        verify(directoryRepository, times(1)).save(any(Directory.class));
    }



}