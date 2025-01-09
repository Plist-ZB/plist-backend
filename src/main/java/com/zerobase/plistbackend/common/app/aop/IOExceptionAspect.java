package com.zerobase.plistbackend.common.app.aop;

import com.zerobase.plistbackend.common.app.exception.JsonParseException;
import com.zerobase.plistbackend.common.app.type.JsonErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class IOExceptionAspect {

  @Around("@annotation(com.zerobase.plistbackend.common.app.aop.IOExceptionHandler)")
  public Object tryCatch(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      String methodName = joinPoint.getSignature().getName();
      String className = joinPoint.getTarget().getClass().getName();

      log.info("AOP TargetClassName = {}, MethodName = {}", className, methodName);

      return joinPoint.proceed();
    } catch (Exception e) {
      log.error("Exception in method: {} with message: {}", joinPoint.getSignature(),
          e.getMessage(), e);

      throw new JsonParseException(JsonErrorStatus.SEVER_ERROR);
    }
  }
}
