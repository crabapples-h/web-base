package cn.crabapples.annotations;

import javax.validation.constraints.NotEmpty;
import java.lang.annotation.*;

/**
 * 自定义注解
 * 2019年1月8日 下午3:02:18
 * @author H
 * TODO
 * Admin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotEmpty
public @interface Crabapples {
    Class [] groups() default {};
}
