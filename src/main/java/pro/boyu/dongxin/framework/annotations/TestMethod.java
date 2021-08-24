package pro.boyu.dongxin.framework.annotations;

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
    @Deprecated
    public boolean sync() default true;
    public int threadsNum() default 1;
    public int maxTime() default 3;
    public int repeatTime() default 1;
}
