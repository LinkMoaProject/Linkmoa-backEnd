package com.linkmoa.source.domain.notification.aop.aspect;

import com.linkmoa.source.domain.notification.aop.proxy.NotificationInfo;
import com.linkmoa.source.domain.notification.constant.NotificationMessage;
import com.linkmoa.source.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class NotificationAspect {

    private final NotificationService notificationService;

    @Pointcut("@annotation(com.linkmoa.source.domain.notification.aop.annotation.NotificationApplied)")
    public void notificationPointcut(){}
    @Async
    @AfterReturning(pointcut = "notificationPointcut()",returning = "result")
    public void createAndSendNotification(JoinPoint joinPoint,Object result) throws Throwable{
        NotificationInfo notificationInfo = (NotificationInfo) result;
        String message = NotificationMessage.getMessageByType(notificationInfo.getNotificationType());
        notificationService.send(
                notificationInfo.getReceiverEmail(),
                notificationInfo.getSenderEmail(),
                notificationInfo.getNotificationType(),
                message,
                "url 테스트중"
        );

        log.info("result = {}",result);

    }






}
