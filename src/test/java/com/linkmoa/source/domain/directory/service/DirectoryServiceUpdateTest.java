package com.linkmoa.source.domain.directory.service;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateRequest;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.member.constant.Role;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.global.command.constant.CommandType;
import com.linkmoa.source.global.dto.request.BaseRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Slf4j
public class DirectoryServiceUpdateTest {

    @InjectMocks
    private DirectoryService directoryService;

    @Mock
    private DirectoryRepository directoryRepository;

    @Mock
    private MemberService memberService;

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
    @DisplayName("directory 수정")
    void testDirectoryUpdate_Success() throws NoSuchFieldException, IllegalAccessException {
        // given
        DirectoryUpdateRequest requestDto = new DirectoryUpdateRequest(
                new BaseRequest(1L, CommandType.EDIT),
                "디렉토리 이름 수정 후",
                "디렉토리 설명 수정 후",
                1L
        );

        Directory directory = Directory.builder()
                .directoryName("Test Directory")
                .directoryDescription("Test Description")
                .build();

        // Reflection으로 ID 설정
        Field idField = Directory.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(directory, 1L);

        // Mock 설정
        when(directoryRepository.findById(1L)).thenReturn(Optional.of(directory));
        // when
        ApiDirectoryResponseSpec<Long> response = directoryService.updateDirectory(requestDto, principalDetails);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals("Directory 수정(이름,설명)에 성공했습니다.", response.getSuccessMessage());
        assertEquals(1L,response.getData());


        assertEquals("디렉토리 이름 수정 후", directory.getDirectoryName());
        assertEquals("디렉토리 설명 수정 후", directory.getDirectoryDescription());


        verify(directoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 디렉토리 수정 시 예외 발생")
    void testDirectoryUpdate_NotFound() {
        // given
        DirectoryUpdateRequest requestDto = new DirectoryUpdateRequest(
                new BaseRequest(1L, CommandType.EDIT),
                "디렉토리 이름 수정 후",
                "디렉토리 설명 수정 후",
                99L // 존재하지 않는 ID
        );

        // Mock 설정
        when(directoryRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        DirectoryException exception = assertThrows(DirectoryException.class,
                () -> directoryService.updateDirectory(requestDto, principalDetails));

        assertEquals(DirectoryErrorCode.DIRECTORY_NOT_FOUND, exception.getDirectoryErrorCode());
        assertEquals("Directory를 찾을 수 없습니다.", exception.getMessage());

        // Verify
        verify(directoryRepository, times(1)).findById(99L); // findById 호출 확인
        verifyNoMoreInteractions(directoryRepository); // 추가 상호작용 없음 확인
    }




}


