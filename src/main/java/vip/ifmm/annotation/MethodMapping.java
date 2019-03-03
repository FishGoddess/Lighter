package vip.ifmm.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 映射方法名和协议中的请求指令
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 20:05:59
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodMapping {

    @AliasFor("instruction")
    String value() default "";

    // 指令和方法映射
    @AliasFor("value")
    String instruction() default "";
}
