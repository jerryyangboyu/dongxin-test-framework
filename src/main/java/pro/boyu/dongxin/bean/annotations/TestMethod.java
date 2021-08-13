package com.uusafe.platform.test.common.bean.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestMethod {
    @Deprecated
    public boolean await() default true;
    public String description() default "";
    public int priority() default 0;
    public boolean sync() default true;
    public int threadsNum() default 1;
    public long maxTime() default 60000;
}
