package com.zerobase.plistbackend.common.app.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 런타임에 접근 가능하도록 설정
@Target(ElementType.METHOD)
public @interface TryCatch {

}
