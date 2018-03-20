package com.phoenix.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class TimeAspect {

    @Around("execution(* com.phoenix.web.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Time Aspect start");

        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            System.out.println(arg);
        }

        long time = new Date().getTime();

        Object result = pjp.proceed();

        System.out.println("Time Aspect 耗时: " + (new Date().getTime()-time));

        System.out.println("Time Aspect end");

        return result;
    }

}
