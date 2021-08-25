package com.fishedee.jpa_boost.lint;

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

    boolean allowIdHaveGeneratedValue() default false;

    Class superEntityClass() default Object.class;

    Class superEmbeddableClass() default Object.class;

    Class<? extends JPALinter>[] extraLinters() default {};
}
