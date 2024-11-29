package com.linkmoa.source.domain.directory.service;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryMoveRequestDto;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.constant.Role;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.entity.Page;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Slf4j
class DirectoryServiceMoveTest {

  /*  @Mock
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
    @DisplayName("같은 부모를 가진 다른 디렉토리로 위치 이동( 드래그 앤 드롭 )")
    void testDirectoryMove_success(){
        DirectoryMoveRequestDto requestDto= new DirectoryMoveRequestDto(
                new BaseRequestDto(1L, CommandType.EDIT),
                2L,
                3L
        );
        Page page = createPage(requestDto.baseRequestDto().pageId(), "page title", "page description", PageType.PERSONAL);

        Directory sourceDirectory = createDirectory(requestDto.sourceDirectoryId(), page, "source Directory", "source Directory");
        Directory targetDirectory = createDirectory(requestDto.targetDirectoryId(), page, "target direcotry", "taget directory ");

        // Mock 설정
        when(directoryRepository.findById(2L)).thenReturn(Optional.of(sourceDirectory));
        when(directoryRepository.findById(3L)).thenReturn(Optional.of(targetDirectory));

        // when
        ApiDirectoryResponseSpec<Long> response = directoryService.moveDirectory(requestDto, principalDetails);

        // then
        assertNotNull(response);
        assertEquals(2L, response.getData());
        assertEquals(targetDirectory, sourceDirectory.getParentDirectory()); // 소스 디렉토리의 부모가 타겟 디렉토리로 변경되었는지 확인
        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals("Directory 위치 이동에 성공했습니다.", response.getSuccessMessage());

        verify(directoryRepository, times(1)).findById(2L); // 소스 디렉토리 조회
        verify(directoryRepository, times(1)).findById(3L); // 타겟 디렉토리 조회

    }

    private Page createPage(Long id,String pageTitle,String pageDescription,PageType pageType){

        Page page = Page.builder()
                .pageTitle(pageTitle)
                .pageDescription(pageDescription)
                .pageType(pageType)
                .build();
        ReflectionTestUtils.setField(page,"id",id);

        return page;

    }
    private Directory createDirectory(Long id,String directoryName,String directoryDescripton){

        Directory directory = Directory.builder()
                .directoryName(directoryName)
                .directoryDescription(directoryDescripton)
                .parentDirectory(null)
                .build();

        ReflectionTestUtils.setField(directory,"id",id);

        return directory;
    }   */




}