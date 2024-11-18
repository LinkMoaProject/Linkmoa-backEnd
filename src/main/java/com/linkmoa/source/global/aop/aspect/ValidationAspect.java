package com.linkmoa.source.global.aop.aspect;


import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.global.command.constant.CommandType;
import com.linkmoa.source.global.command.service.CommandService;
import com.linkmoa.source.global.dto.request.BaseRequestDto;
import com.linkmoa.source.global.error.code.impl.ValidationErrorCode;
import com.linkmoa.source.global.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@Aspect
@AllArgsConstructor
public class ValidationAspect {


    private final CommandService commandService;


    @Pointcut("@annotation(com.linkmoa.source.global.aop.annotation.ValidationApplied)")
    public void validationPointCut(){}


    /**
     * 여기서 검증을 시도
     * => 만약, 여기서 올바른 권한이 없다면 => 올바른 권한이 없다고 출력해주기
     * => 만약, 올바른 권한이 있다면 => 그냥 메서드를 실행하면됨.
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("validationPointCut() && args(baseRequestDto)")
    public Object validate(ProceedingJoinPoint joinPoint,final BaseRequestDto baseRequestDto) throws Throwable {

        Long memberId = baseRequestDto.memberId();
        Long pageId = baseRequestDto.pageId();
        CommandType commandType = baseRequestDto.commandType();

        PermissionType userPermissionType = commandService.getUserPermissionType(memberId, pageId);

        if (!commandService.canExecute(userPermissionType, commandType)) {
            log.info("해당 요청에 관한 권한이 없습니다. userPermissionType={} , commandType={} ",userPermissionType,commandType);
            throw new ValidationException(ValidationErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 타겟 메서드 실행
        Object result = joinPoint.proceed();
        return result;
    }

}
