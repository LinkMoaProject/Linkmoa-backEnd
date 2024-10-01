package com.linkmoa.source.global.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@Aspect
public class LogAspect {


    /**
     * execution(* com.linkmoa.source.global..*.*(..))
     * 1."*" : 모든 반환 타입
     * 2."com.linkmoa.source.global.."  : com.linkmoa.source.global 패키지의 모든 하위 패키지
     * 3. "*" : 모든 클래스를 의미함.
     * 4. (..) : 모든 매개변수 가능
     */


    @Pointcut("execution(* com.linkmoa.source.domain.*.*(..)) && !execution(* com.linkmoa.source.global..*.*(..))")
    public void allDomain(){}

    @Pointcut("execution(* com.linkmoa.source.domain..*Controller.*(..))")
    public void controller(){}

    @Around("allDomain()")
    public Object logging(ProceedingJoinPoint joinPoint) throws  Throwable{
        long start = System.currentTimeMillis();

        try{
            Object result = joinPoint.proceed();
            return result;
        }finally {
            long end = System.currentTimeMillis();
            long timeinMs = end - start;
            log.info("[ {} ] | time = {}ms ", joinPoint.getSignature(), timeinMs);
        }
    }


    /**
     * 문제점 : @RequestBody에 있는 request의 파라미터의 키 밸류를 가져와서 로그로 출력하고 싶음
     * 하지만, 필터나 인터셉터에서 @RequestBody에서 사용된 객체를 불러와서 읽으면, 필터나 인터셉터에서는 사용할 수 있으나, Controller까지 넘어가지 않음.
     * 즉, 한번 사용되면 재사용되지 않는 성질이 있어 필터나 인터셉터에서 @RequestBody 데이터를 중간에 한번 읽으려면 Wrapper를 통해서 감싸줘야 함.
     */
    @Around("controller()")
    public Object loggingController(ProceedingJoinPoint joinPoint) throws Throwable{

        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        Map<String,Object> parmas = new HashMap<>();


        try {
            String decodedURI = URLDecoder.decode(request.getRequestURI(), "UTF-8");

            parmas.put("controller", controllerName);
            parmas.put("method", methodName);
            parmas.put("params", getParams(request));
            parmas.put("log_time", System.currentTimeMillis());
            parmas.put("request_uri", decodedURI);
            parmas.put("http_method", request.getMethod());

        }catch (Exception e){
            log.error("LoggerAspect error",e);
        }

        log.info("[ {} ] {}", parmas.get("http_method"), parmas.get("request_uri"));
        log.info("[ method ] {}.{} ", parmas.get("controller"), parmas.get("method"));
        log.info("[ params ] {}", parmas.get("params"));

        Object result = joinPoint.proceed();

        return result;
    }

    private static JSONObject getParams(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String param = params.nextElement();
            jsonObject.put(param, request.getParameter(param));
        }


        return jsonObject;
    }


}
