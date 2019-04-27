package top.liuliyong.account.interceptor.annotation;

import java.lang.annotation.*;

/**
 * 标记需要验证session_id的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CheckSessionId {
}
