package com.daniel.bookshop.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionAspect {
    private static final Logger logger = LogManager.getLogger(ExceptionAspect.class);

    @AfterThrowing(pointcut = "execution(* com.daniel.bookshop..*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        logger.error("Exception in {}.{}() with arguments: {}", className, methodName, args, ex);
    }
} 