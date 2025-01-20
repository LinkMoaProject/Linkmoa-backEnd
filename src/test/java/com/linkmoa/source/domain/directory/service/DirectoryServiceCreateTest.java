package com.linkmoa.source.domain.directory.service;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateReques;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.entity.Directory;
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

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class DirectoryServiceCreateTest {

    @InjectMocks  //DI를 @Mock 이나 @Spy로 생성된 mock 객체를 자동으로 주입해줌.
    private DirectoryService directoryService;
    @Mock // 실제 객체와 동일한 모의 객체 Mock 객체를 만들어줌.
    private DirectoryRepository directoryRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private PrincipalDetails principalDetails;

    @BeforeEach
    // 현재 클래스의 각 @Test 메소드보다 먼저 실행되어야함.
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
    @DisplayName("parent directory가 없는 경우 directory 생성 테스트 ")
    public void createDirectory_WithoutParentDirectory_Test() {
        // given
        DirectoryCreateReques requestDto = new DirectoryCreateReques(
                new BaseRequest(1L, CommandType.CREATE),
                "Test Directory",
                null,
                "Test Description"
        );

        // when : 특정 메서드 호출될 떄 실행될 동작(Stub)을 지정
        // directoryRepository.save 메서드 호출시 동작을 Mock 으로 설정
        // 테스트 중에 실제 데이터베이스를 사용하지 않고, 저장되는 Directory 객체의 동작을 제어
        // invocation : save 메서드 호출에 대한 정보를 담음 - 호출된 메서드의 인자/반환값 등을 다룰 수 있음.
        when(directoryRepository.save(any(Directory.class))).thenAnswer(invocation -> {
            Directory savedDirectory = invocation.getArgument(0); //save 메서드 호출시 전달된 첫번째 인자
            // 테스트 환경에서는 JPA의 자동 ID 생성(@GeneratedValue)이 동작하지 않으므로,
            // 테스트 중 ID를 직접 설정하기 위해 리플렉션을 사용.
            // Directory 클래스의 id 라는 이름의 필드를 가져옴.
            Field idField = Directory.class.getDeclaredField("id");
            idField.setAccessible(true); //필드에 대한 접근 권한 부여
            idField.set(savedDirectory, 1L);  // ID를 강제로 1L로 설정

            return savedDirectory; // Mock의 반환값 : save 메서드로 저장된 엔터티 객체
        });

        ApiDirectoryResponseSpec<Long> response = directoryService.createDirectory(requestDto, principalDetails);

        // then
        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals("Directory 생성에 성공했습니다.", response.getSuccessMessage());
        assertNotNull(response.getData()); //null이 아님을 확인함.

        // 검증
        //directoryRepository.save 메서드가 정확히 한 번 호출되었는지 확인함.
        verify(directoryRepository, times(1)).save(any(Directory.class));
    }


    @Test
    @DisplayName("parent directory가 있는 경우 directory 생성 테스트 ")
    void createDirectory_WithParentDirectory_Test() throws Exception {

        DirectoryCreateReques requestDto = new DirectoryCreateReques(
                new BaseRequest(1L, CommandType.CREATE),
                "Test Sub Directory",
                2L,
                "Test SubDescription"
        );

        Directory parentDirectory = Directory.builder()
                .directoryName("Parent Directory")
                .directoryDescription("Parent Description")
                .build();

        Directory newDirectory = Directory.builder()
                .directoryName(requestDto.directoryName())
                .directoryDescription(requestDto.directoryDescription())
                .build();

        parentDirectory.addChildDirectory(newDirectory);

        // Mock 동작 설정
        when(directoryRepository.findById(2L)).thenReturn(Optional.of(parentDirectory));
        when(directoryRepository.save(any(Directory.class))).thenAnswer(invocation -> {
            Directory savedDirectory = invocation.getArgument(0);

            // 리플렉션으로 Directory의 id 필드에 값 설정
            Field idField = Directory.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(savedDirectory, 3L); // ID를 강제로 설정

            return savedDirectory;
        });

        // when
        ApiDirectoryResponseSpec<Long> response = directoryService.createDirectory(requestDto, principalDetails);

        // then
        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals("Directory 생성에 성공했습니다.", response.getSuccessMessage());
        assertNotNull(response.getData()); // null이 아님을 확인
        assertEquals(3L, response.getData()); // 반환된 ID가 예상대로 1L인지 확인

        // 검증
        verify(directoryRepository, times(1)).findById(2L);
        verify(directoryRepository, times(1)).save(any(Directory.class));
    }
}