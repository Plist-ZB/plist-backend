package com.zerobase.plistbackend.common.app.aop;

import com.zerobase.plistbackend.common.app.exception.JsonParseException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TryCatchAspect {

  @Around("@annotation(com.zerobase.plistbackend.common.app.aop.TryCatch)")
  public Object tryCatch(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      return joinPoint.proceed();
    } catch (IOException e) {
      log.error("Exception in method: {} with message: {}", joinPoint.getSignature(),
          e.getMessage(), e);
      throw new JsonParseException(e.getMessage());
    }
  }
}
