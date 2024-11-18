package com.linkmoa.source.global.aop.aspect;

import com.linkmoa.source.domain.member.constant.Role;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.global.command.constant.CommandType;
import com.linkmoa.source.global.command.service.CommandService;
import com.linkmoa.source.global.dto.request.BaseRequestDto;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ValidationAspectTest {

    @Mock
    private CommandService commandService;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @InjectMocks
    private ValidationAspect validationAspect;

    private BaseRequestDto baseRequestDto;

    @BeforeEach
    void setUp() {
        //given
        baseRequestDto = new BaseRequestDto(
                1L,           // memberId
                1L,           // pageId
                CommandType.EDIT  // commandType
        );
    }

    @Test
    void validate_WithAuthorizedAccess_ShouldProceed() throws Throwable {

        // 권한이 있는 경우
        // when
        when(commandService.getUserPermissionType(1L, 1L)).thenReturn(PermissionType.HOST);
        when(commandService.canExecute(PermissionType.HOST, CommandType.EDIT)).thenReturn(true);
        when(proceedingJoinPoint.proceed()).thenReturn("Proceed Success");

        // 메서드 실행
        Object result = validationAspect.validate(proceedingJoinPoint, baseRequestDto);

        // 검즘
        // than
        assertEquals("Proceed Success", result);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    void validate_WithUnauthorizedAccess_ShouldThrowException() throws Throwable {
        // 권한이 없는 경우
        when(commandService.getUserPermissionType(1L, 1L)).thenReturn(PermissionType.VIEWER);
        when(commandService.canExecute(PermissionType.VIEWER, CommandType.EDIT)).thenReturn(false);

        // 예외가 발생하는지 검증
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationAspect.validate(proceedingJoinPoint, baseRequestDto);
        });

        assertEquals(ValidationErrorCode.UNAUTHORIZED_ACCESS, exception.getValidationErrorCode());

        // 타겟 메서드가 호출되지 않는지 확인
       verify(proceedingJoinPoint, never()).proceed();
    }






}