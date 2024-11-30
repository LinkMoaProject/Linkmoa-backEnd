package com.linkmoa.source.domain.notify.aop.aspect;

import com.linkmoa.source.domain.notify.aop.proxy.NotifyInfo;
import com.linkmoa.source.domain.notify.constant.NotificationType;
import com.linkmoa.source.domain.notify.constant.NotifyMessage;
import com.linkmoa.source.domain.notify.service.NotifyService;
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
public class NotifyAspect {

    private final NotifyService notifyService;

    @Pointcut("@annotation(com.linkmoa.source.domain.notify.aop.annotation.NotifyApplied)")
    public void notifyPointcut(){}
    @Async
    @AfterReturning(pointcut = "notifyPointcut()",returning = "result")
    public void createAndSendNotification(JoinPoint joinPoint,Object result) throws Throwable{
        NotifyInfo notifyInfo = (NotifyInfo) result;
        String message = NotifyMessage.getMessageByType(notifyInfo.getNotificationType());
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
