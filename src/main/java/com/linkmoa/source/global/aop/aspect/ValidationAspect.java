package com.linkmoa.source.global.aop.aspect;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;

import com.linkmoa.source.global.command.service.CommandService;
import com.linkmoa.source.global.constant.CommandType;
import com.linkmoa.source.global.dto.request.BaseRequest;
import com.linkmoa.source.global.error.code.impl.ValidationErrorCode;
import com.linkmoa.source.global.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;


@Component
@Slf4j
@Aspect
@AllArgsConstructor
public class ValidationAspect {

    private final CommandService commandService;
    private final MemberService memberService;

    @Pointcut("@annotation(com.linkmoa.source.global.aop.annotation.ValidationApplied)")
    public void validationPointCut(){}


    @Around("validationPointCut() && args(requestDto,principalDetails)")
    public Object validate(ProceedingJoinPoint joinPoint,final Object requestDto,final PrincipalDetails principalDetails) throws Throwable {

        BaseRequest baseRequest = null;

        for (Field field : requestDto.getClass().getDeclaredFields()) {
            if (field.getType().equals(BaseRequest.class)) {
                field.setAccessible(true); // private 필드 접근 허용
                baseRequest = (BaseRequest) field.get(requestDto); // BaseRequestDto 필드 추출
                break;
            }
        }

        if (baseRequest == null) {
            throw new ValidationException(ValidationErrorCode.MISSING_BASE_REQUEST_DTO);
        }

        Member member = memberService.findMemberByEmail(principalDetails.getEmail());

        Long pageId = baseRequest.pageId();
        CommandType commandType = baseRequest.commandType();

        PermissionType userPermissionType = commandService.getUserPermissionType(member.getId(), pageId);

        if (!commandService.canExecute(userPermissionType, commandType)) {
            log.info("해당 요청에 관한 권한이 없습니다. userPermissionType={} , commandType={} ",userPermissionType,commandType);
            throw new ValidationException(ValidationErrorCode.UNAUTHORIZED_ACCESS);
        }

        log.info("AOP ValidationAspect triggered with BaseRequestDto: {}", baseRequest);
        log.info("member email : {}", principalDetails.getEmail());
        log.info("userPermissionType={} , commandType={} ",userPermissionType,commandType);
        // 타겟 메서드 실행
        Object result = joinPoint.proceed();
        return result;
    }

}
