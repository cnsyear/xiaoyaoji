package cn.xiaoyaoji.service.annotations;

import java.lang.annotation.ElementType;

/**
 * 如果在controller中使用，则表示该类或方法不被权限拦截
 *
 * @author: zhoujingjie
 * @Date: 16/5/2
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface Ignore {
    boolean value() default true;
}
