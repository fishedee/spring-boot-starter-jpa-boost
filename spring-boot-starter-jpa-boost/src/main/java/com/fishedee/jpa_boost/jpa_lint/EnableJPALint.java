package com.fishedee.jpa_boost.jpa_lint;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ImportJPACheckRegistrar.class)
public @interface EnableJPALint {
    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};
}
