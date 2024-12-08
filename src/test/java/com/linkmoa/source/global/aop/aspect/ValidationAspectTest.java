package com.linkmoa.source.global.aop.aspect;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.constant.Role;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;

import com.linkmoa.source.global.command.service.CommandService;
import com.linkmoa.source.global.command.constant.CommandType;
import com.linkmoa.source.global.dto.request.BaseRequest;
import com.linkmoa.source.global.error.code.impl.ValidationErrorCode;
import com.linkmoa.source.global.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ValidationAspectTest {

    @Mock
    private CommandService commandService;

    @Mock
    private MemberService memberService;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @InjectMocks
    private ValidationAspect validationAspect;

    private BaseRequest baseRequest;

    private  TestRequestDto requestDto;
    private PrincipalDetails principalDetails;

    // 새 DTO 정의 (BaseRequestDto를 포함)
    class TestRequestDto {
        private BaseRequest baseRequest;

        public TestRequestDto(BaseRequest baseRequest) {
            this.baseRequest = baseRequest;
        }

        public BaseRequest getBaseRequestDto() {
            return baseRequest;
        }
    }
    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // BaseRequestDto 생성
        baseRequest = new BaseRequest(
                1L,           // pageId
                CommandType.EDIT  // commandType
        );

        // TestRequestDto 생성
        requestDto = new TestRequestDto(baseRequest);


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

        // memberService의 동작 설정
        when(memberService.findMemberByEmail("test@example.com")).thenReturn(member);
    }

    @Test
    void validate_WithAuthorizedAccess_ShouldProceed() throws Throwable {


        // 권한이 있는 경우
        when(commandService.getUserPermissionType(principalDetails.getId(), baseRequest.pageId())).thenReturn(PermissionType.HOST);
        when(commandService.canExecute(PermissionType.HOST, CommandType.EDIT)).thenReturn(true);
        when(proceedingJoinPoint.proceed()).thenReturn("Proceed Success");

        // 메서드 실행
        Object result = validationAspect.validate(proceedingJoinPoint, requestDto, principalDetails);

        // 검증
        assertEquals("Proceed Success", result);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    void validate_WithUnauthorizedAccess_ShouldThrowException() throws Throwable {
        // 권한이 없는 경우
        when(commandService.getUserPermissionType(principalDetails.getId(), baseRequest.pageId())).thenReturn(PermissionType.VIEWER);
        when(commandService.canExecute(PermissionType.VIEWER, CommandType.EDIT)).thenReturn(false);

        // 예외가 발생하는지 검증
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationAspect.validate(proceedingJoinPoint, requestDto, principalDetails);
        });

        assertEquals(ValidationErrorCode.UNAUTHORIZED_ACCESS, exception.getValidationErrorCode());

        // 타겟 메서드가 호출되지 않는지 확인
        verify(proceedingJoinPoint, never()).proceed();
    }


}