package top.liuliyong.account.interceptor.annotation;

import java.lang.annotation.*;

/**
 * 标记需要管理员权限的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NeedAdminAuth {
}
