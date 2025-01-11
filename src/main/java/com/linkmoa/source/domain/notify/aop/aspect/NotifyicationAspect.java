package com.linkmoa.source.domain.notify.aop.aspect;

import com.linkmoa.source.domain.notify.aop.proxy.NotificationInfo;
import com.linkmoa.source.domain.notify.constant.NotificationMessage;
import com.linkmoa.source.domain.notify.service.NotificationService;
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
public class NotifyicationAspect {

    private final NotificationService notifyService;

    @Pointcut("@annotation(com.linkmoa.source.domain.notify.aop.annotation.NotificationApplied)")
    public void notifyPointcut(){}
    @Async
    @AfterReturning(pointcut = "notifyPointcut()",returning = "result")
    public void createAndSendNotification(JoinPoint joinPoint,Object result) throws Throwable{
        NotificationInfo notifyInfo = (NotificationInfo) result;
        String message = NotificationMessage.getMessageByType(notifyInfo.getNotificationType());
        notifyService.send(
                notifyInfo.getReceiverEmail(),
                notifyInfo.getSenderEmail(),
                notifyInfo.getNotificationType(),
                message,
                "url 테스트중"
        );

        log.info("result = {}",result);

    }






}
