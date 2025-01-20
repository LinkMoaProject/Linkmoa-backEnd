package com.linkmoa.source.domain.directory.service;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryIdRequest;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.linkmoa.source.global.command.constant.CommandType.EDIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class DirectoryServiceDeleteTest {

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
    @DisplayName("존재하는 디렉토리 삭제 테스트")
    void directoryDelete_Success() throws NoSuchFieldException, IllegalAccessException {

        // Given
        DirectoryIdRequest requestDto = new DirectoryIdRequest(
                new BaseRequest(1L, CommandType.EDIT), 1L);

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
        // Mock delete 동작 설정
        doAnswer(invocation -> {
            Directory deletedDirectory = invocation.getArgument(0);

            // 리플렉션으로 Directory의 id 필드에 값 설정 (테스트 시 동작 확인용)
            Field idFieldInner = Directory.class.getDeclaredField("id");
            idFieldInner.setAccessible(true);
            idFieldInner.set(deletedDirectory, 1L); // ID를 강제로 설정

            log.info("Deleted Directory ID: {}", deletedDirectory.getId());
            return null; // void 메서드이므로 null 반환
        }).when(directoryRepository).delete(any(Directory.class));

        // When
        ApiDirectoryResponseSpec<Long> response = directoryService.deleteDirectory(requestDto, principalDetails);

        // Then
        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals("Directory 삭제에 성공했습니다.", response.getSuccessMessage());
        assertEquals(1L, response.getData()); // 반환된 ID 확인
        log.info(response.getSuccessMessage());

        // Verify
        verify(directoryRepository, times(1)).delete(any(Directory.class)); // delete 호출 검증
    }

    @Test
    @DisplayName("존재하지 않는 디렉토리 삭제 시 예외 발생 테스트")
    void directoryDelete_NotFound() {
        // Given
        DirectoryIdRequest requestDto = new DirectoryIdRequest(
                new BaseRequest(1L, CommandType.EDIT), 99L); // 존재하지 않는 ID 설정

        // Mock 설정
        when(directoryRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        DirectoryException exception = assertThrows(
                DirectoryException.class,
                () -> directoryService.deleteDirectory(requestDto, principalDetails)
        );

        // 예외 검증
        assertEquals(DirectoryErrorCode.DIRECTORY_NOT_FOUND, exception.getDirectoryErrorCode());

        // Verify
        verify(directoryRepository, times(1)).findById(99L); // findById가 한 번 호출되었는지 검증
        verify(directoryRepository, never()).delete(any());  // delete가 호출되지 않았는지 검증
    }


}